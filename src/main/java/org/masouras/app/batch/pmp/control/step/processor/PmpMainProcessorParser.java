package org.masouras.app.batch.pmp.control.step.processor;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.masouras.boundary.PrintingDataEntityProcessor;
import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PmpMainProcessorParser implements ItemProcessor<PrintingDataEntity, PrintingDataEntity> {
    private final PrintingDataEntityProcessor printingDataEntityProcessor;

    @Override
    public PrintingDataEntity process(@NotNull PrintingDataEntity printingDataEntity) {
        return printingDataEntityProcessor.processPrintingDataEntity(printingDataEntity);
    }
}

