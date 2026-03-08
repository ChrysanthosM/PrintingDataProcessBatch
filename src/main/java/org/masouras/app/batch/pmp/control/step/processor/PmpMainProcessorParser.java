package org.masouras.app.batch.pmp.control.step.processor;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.masouras.app.artemis.business.printing.model.PrintingJobMessage;
import org.masouras.app.artemis.business.printing.producer.ArtemisPrintingJobProducer;
import org.masouras.data.boundary.PrintingDataEntityProcessor;
import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PmpMainProcessorParser implements ItemProcessor<PrintingDataEntity, PrintingDataEntity> {
    private final PrintingDataEntityProcessor printingDataEntityProcessor;
    private final ArtemisPrintingJobProducer artemisPrintingJobProducer;

    @Override
    public PrintingDataEntity process(@NotNull PrintingDataEntity printingDataEntity) {
        switch (printingDataEntity.getPrintingWayType()) {
            case BATCH -> {
                return printingDataEntityProcessor.processPrintingDataEntity(printingDataEntity);
            }
            case ARTEMIS -> {
                artemisPrintingJobProducer.send(new PrintingJobMessage(printingDataEntity.getId()));
                return printingDataEntity;
            }
            default -> throw new IllegalStateException("Unexpected value: " + printingDataEntity.getPrintingWayType());
        }
    }
}

