package org.masouras.app.batch.pmp.control.step.business;

import lombok.extern.slf4j.Slf4j;
import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;
import org.masouras.model.mssql.schema.jpa.control.entity.enums.PrintingStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public non-sealed class MainProcessFinalizeService implements MainProcessBase {

    @Override
    public PrintingDataEntity processPrintingDataEntity(PrintingDataEntity printingDataEntity) {
        if (log.isInfoEnabled()) log.info("{}: Finalizing printingDataEntity {}", this.getClass().getSimpleName(), printingDataEntity.getId());
        printingDataEntity.setPrintingStatus(PrintingStatus.PROCESSED);
        return printingDataEntity;
    }
}
