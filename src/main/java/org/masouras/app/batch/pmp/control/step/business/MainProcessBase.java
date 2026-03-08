package org.masouras.app.batch.pmp.control.step.business;

import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;

public sealed interface MainProcessBase permits MainProcessValidationService, MainProcessFinalizeService {
    PrintingDataEntity processPrintingDataEntity(PrintingDataEntity printingDataEntity);
}
