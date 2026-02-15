package org.masouras.app.batch.pmp.control.step.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.masouras.app.batch.pmp.control.step.business.MainProcessValidationService;
import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PmpMainProcessorValidation implements ItemProcessor<PrintingDataEntity, PrintingDataEntity> {
    private final MainProcessValidationService mainProcessValidationService;

    @Override
    public PrintingDataEntity process(@NotNull PrintingDataEntity printingDataEntity) {
        return mainProcessValidationService.processPrintingDataEntity(printingDataEntity);
    }
}

