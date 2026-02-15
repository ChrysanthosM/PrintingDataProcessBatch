package org.masouras.app.batch.pmp.control.step.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.masouras.app.batch.pmp.control.step.business.MainProcessFinalizeService;
import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PmpMainProcessorFinalize implements ItemProcessor<PrintingDataEntity, PrintingDataEntity> {
    private final MainProcessFinalizeService mainProcessFinalizeService;

    @Override
    public PrintingDataEntity process(@NotNull PrintingDataEntity printingDataEntity) {
        return mainProcessFinalizeService.processPrintingDataEntity(printingDataEntity);
    }
}

