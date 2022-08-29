package apple.utilities.database.ajd;

import com.google.gson.Gson;

public interface IAppleAJD {
    void setSerializing(AppleAJD.AppleAJDSerializer serializer, AppleAJD.AppleAJDDeserializer deserializer);

    void setSerializingJson();

    void setSerializingJson(Gson gson);

    void setSerializingYaml();
}
