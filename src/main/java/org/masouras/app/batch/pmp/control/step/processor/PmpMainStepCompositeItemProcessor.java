package org.masouras.app.batch.pmp.control.step.processor;

import org.masouras.model.maria.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PmpMainStepCompositeItemProcessor extends CompositeItemProcessor<PrintingDataEntity, PrintingDataEntity> {

    @Autowired
    public PmpMainStepCompositeItemProcessor(
            PmpMainProcessorValidation validation,
            PmpMainProcessorParser parser) {

        setDelegates(Arrays.asList(validation, parser));
    }
}
