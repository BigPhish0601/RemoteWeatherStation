// To parse this JSON data, add NuGet 'Newtonsoft.Json' then do:
//
//    using \;
//
//    var weatherDataDto = WeatherDataDto.FromJson(jsonString);


using System.Globalization;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

public partial class WeatherDataDto
{
    [JsonProperty("date_time")]
    public long DateTime { get; set; }

    [JsonProperty("humidity")]
    public double Humidity { get; set; }

    [JsonProperty("temp_in_f")]
    public double TempInF { get; set; }
}

public partial class WeatherDataDto
{
    public static WeatherDataDto FromJson(string json) => JsonConvert.DeserializeObject<WeatherDataDto>(json, Converter.Settings);
}

public static class Serialize
{
    public static string ToJson(this WeatherDataDto self) => JsonConvert.SerializeObject(self, Converter.Settings);
}

internal static class Converter
{
    public static readonly JsonSerializerSettings Settings = new JsonSerializerSettings
    {
        MetadataPropertyHandling = MetadataPropertyHandling.Ignore,
        DateParseHandling = DateParseHandling.None,
        Converters = {
                new IsoDateTimeConverter { DateTimeStyles = DateTimeStyles.AssumeUniversal }
            },
    };
}

