package org.masouras.app.batch.pmp.control.step.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.masouras.data.boundary.FilesFacade;
import org.masouras.data.boundary.RepositoryFacade;
import org.masouras.data.control.parser.FileValidator;
import org.masouras.data.control.parser.FileValidatorFactory;
import org.masouras.data.domain.FileValidatorResult;
import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public non-sealed class MainProcessValidationService implements MainProcessBase {
    private final FileValidatorFactory fileValidatorFactory;
    private final FilesFacade filesFacade;
    private final RepositoryFacade repositoryFacade;

    @Override
    public PrintingDataEntity processPrintingDataEntity(PrintingDataEntity printingDataEntity) {
        if (log.isInfoEnabled()) log.info("{}: Validating printingDataEntity {}", this.getClass().getSimpleName(), printingDataEntity.getId());
        return saveContentValidated(printingDataEntity, getFileValidatorResult(printingDataEntity));
    }

    public FileValidatorResult getFileValidatorResult(PrintingDataEntity printingDataEntity) {
        FileValidator fileValidator = fileValidatorFactory.getFileValidator(printingDataEntity.getFileExtensionType().name());
        if (fileValidator == null) throw new ValidationException("Validation failed, FileExtensionType not found: " + printingDataEntity.getFileExtensionType().name());
        FileValidatorResult fileValidatorResult = fileValidator.getValidatedResult((Object) printingDataEntity.getInitialContent().getContentBinary());
        if (fileValidatorResult.getStatus() == FileValidatorResult.ValidationStatus.ERROR) throw new ValidationException("Validation failed with message: " + fileValidatorResult.getMessage());
        return fileValidatorResult;
    }

    private PrintingDataEntity saveContentValidated(@NonNull PrintingDataEntity printingDataEntity, FileValidatorResult fileValidatorResult) {
        try {
            String stringDocument = filesFacade.documentToString((Document) fileValidatorResult.getResult());
            byte[] bytesDocument = stringDocument.getBytes(StandardCharsets.UTF_8);
            return repositoryFacade.saveContentValidated(printingDataEntity, bytesDocument);
        } catch (TransformerException e) {
            log.error("{} failed with message: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            throw new ValidationException("Validation failed with message: " + e.getMessage(), e);
        }
    }
}
