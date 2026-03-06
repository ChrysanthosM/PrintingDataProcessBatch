package org.masouras.app.batch.pmp.control.step.processor;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.masouras.app.batch.pmp.control.step.business.MainProcessParserService;
import org.masouras.app.rabbit.business.printing.model.PrintJobMessage;
import org.masouras.app.rabbit.business.printing.producer.PrintingJobProducer;
import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PmpMainProcessorParser implements ItemProcessor<PrintingDataEntity, PrintingDataEntity> {
    private final MainProcessParserService mainProcessParserService;
    private final PrintingJobProducer printingJobProducer;

    @Override
    public PrintingDataEntity process(@NotNull PrintingDataEntity printingDataEntity) {
        switch (printingDataEntity.getPrintingWayType()) {
            case BATCH -> {
                return mainProcessParserService.processPrintingDataEntity(printingDataEntity);
            }
            case RABBIT -> {
                printingJobProducer.sendMediumPriority(new PrintJobMessage(printingDataEntity.getId()));
                return printingDataEntity;
            }
            default -> throw new IllegalStateException("Unexpected value: " + printingDataEntity.getPrintingWayType());
        }
    }
}

