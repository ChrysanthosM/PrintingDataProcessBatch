package org.masouras.app.batch.pmp.control.step.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.masouras.data.boundary.FilesFacade;
import org.masouras.data.boundary.RepositoryFacade;
import org.masouras.data.control.parser.FileProcessor;
import org.masouras.data.control.parser.FileProcessorFactory;
import org.masouras.data.domain.FileProcessorResult;
import org.masouras.model.mssql.schema.jpa.control.entity.PrintingDataEntity;
import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public non-sealed class MainProcessParserService implements MainProcessBase {
    private final FileProcessorFactory fileProcessorFactory;
    private final FilesFacade filesFacade;
    private final RepositoryFacade repositoryFacade;

    @Override
    public PrintingDataEntity processPrintingDataEntity(PrintingDataEntity printingDataEntity) {
        if (log.isInfoEnabled()) log.info("{}: Parsing printingDataEntity {}", this.getClass().getSimpleName(), printingDataEntity.getId());
        return saveContentValidated(printingDataEntity, getFileProcessorResult(printingDataEntity));
    }

    private FileProcessorResult getFileProcessorResult(PrintingDataEntity printingDataEntity) {
        FileProcessor fileProcessor = fileProcessorFactory.getFileProcessor(printingDataEntity.getFileExtensionType().name());
        if (fileProcessor == null) { throw new ValidationException("Parser failed, FileExtensionType not found: " + printingDataEntity.getFileExtensionType().name()); }
        FileProcessorResult fileProcessorResult = fileProcessor.getFileProcessorResult(printingDataEntity.getActivity().getActivityType(), printingDataEntity.getContentType(), printingDataEntity.getValidatedContent().getContentBinary());
        if (fileProcessorResult.getStatus() == FileProcessorResult.ProcessorStatus.ERROR) throw new ValidationException("Parser failed with message: " + fileProcessorResult.getMessage());
        return fileProcessorResult;
    }

    private PrintingDataEntity saveContentValidated(@NonNull PrintingDataEntity printingDataEntity, FileProcessorResult fileProcessorResult) {
        try {
            String stringDocument = filesFacade.documentToString((Document) fileProcessorResult.getResult());
            byte[] bytesDocument = stringDocument.getBytes(StandardCharsets.UTF_8);
            return repositoryFacade.saveContentValidated(printingDataEntity, bytesDocument);
        } catch (TransformerException e) {
            log.error("{} failed with message: {}", this.getClass().getSimpleName(), e.getMessage(), e);
            throw new ValidationException("Validation failed with message: " + e.getMessage(), e);
        }
    }
}
