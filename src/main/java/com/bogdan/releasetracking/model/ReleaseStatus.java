package com.bogdan.releasetracking.model;

import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name = "releaseStatus")
public enum ReleaseStatus {
    CREATED,
    IN_DEVELOPMENT,
    ON_DEV,
    QA_DONE_ON_DEV,
    ON_STAGING,
    QA_DONE_STAGING,
    ON_PROD,
    DONE
}
