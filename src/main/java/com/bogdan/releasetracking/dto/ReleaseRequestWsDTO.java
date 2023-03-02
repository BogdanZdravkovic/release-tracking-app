package com.bogdan.releasetracking.dto;

import com.bogdan.releasetracking.model.ReleaseStatus;
import com.bogdan.releasetracking.validator.ValueOfEnum;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

public class ReleaseRequestWsDTO {

    @ApiModelProperty(name="name")
    private String name;

    @ApiModelProperty(name="description")
    private String description;

    @ValueOfEnum(enumClass = ReleaseStatus.class)
    @ApiModelProperty(name="status")
    @Nullable
    private String status;

    @ApiModelProperty(name="release_date")
    private String releaseDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
