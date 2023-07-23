package org.example.database.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditEntity is a Querydsl query type for AuditEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAuditEntity extends EntityPathBase<AuditEntity<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 904523960L;

    public static final QAuditEntity auditEntity = new QAuditEntity("auditEntity");

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.time.Instant> modifiedAt = createDateTime("modifiedAt", java.time.Instant.class);

    public final StringPath modifiedBy = createString("modifiedBy");

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAuditEntity(String variable) {
        super((Class) AuditEntity.class, forVariable(variable));
    }

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAuditEntity(Path<? extends AuditEntity> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAuditEntity(PathMetadata metadata) {
        super((Class) AuditEntity.class, metadata);
    }

}

