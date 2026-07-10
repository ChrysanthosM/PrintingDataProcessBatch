package org.masouras.app.batch.pmp.control.step.processor;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.masouras.facade.PrintingDataEntityFacade;
import org.masouras.model.maria.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PmpMainProcessorParser implements ItemProcessor<PrintingDataEntity, PrintingDataEntity> {
    private final PrintingDataEntityFacade printingDataEntityFacade;

    @Override
    public PrintingDataEntity process(@NotNull PrintingDataEntity printingDataEntity) {
        return printingDataEntityFacade.processPrintingDataEntity(printingDataEntity);
    }
}

