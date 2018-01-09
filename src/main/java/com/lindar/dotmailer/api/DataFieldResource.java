package com.lindar.dotmailer.api;

import com.google.gson.reflect.TypeToken;
import com.lindar.dotmailer.util.DefaultEndpoints;
import com.lindar.dotmailer.vo.api.DataField;
import com.lindar.dotmailer.vo.internal.DMAccessCredentials;
import com.lindar.wellrested.vo.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DataFieldResource extends AbstractResource {

    public DataFieldResource(DMAccessCredentials accessCredentials) {
        super(accessCredentials);
    }

    public Result<List<DataField>> list() {
        String initialPath = DefaultEndpoints.DATA_FIELDS.getPath();

        return sendAndGetSingleList(initialPath, new TypeToken<List<DataField>>() {});
    }

    public Result<DataField> create(DataField dataField) {
        String path = DefaultEndpoints.DATA_FIELDS.getPath();
        return postAndGet(path, dataField);
    }

    public Result delete(String name) {
        return super.delete(pathWithParam(DefaultEndpoints.DATA_FIELD.getPath(), name));
    }

}
