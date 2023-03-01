package com.bogdan.releasetracking.dto;

import com.bogdan.releasetracking.model.ReleaseStatus;
import com.bogdan.releasetracking.validator.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class UpdateReleaseWsDTO {

    @ApiModelProperty(name="name")
    private String name;

    @ApiModelProperty(name="name")
    private String description;

    @ValueOfEnum(enumClass = ReleaseStatus.class)
    @ApiModelProperty(name="name")
    private String status;

    @ApiModelProperty(name="name")
    @JsonProperty("release_date")
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
