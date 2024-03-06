// To parse this JSON data, add NuGet 'Newtonsoft.Json' then do:
//
//    using \;
//
//    var betweenDates = BetweenDates.FromJson(jsonString);

namespace weatherstation.Controllers
{
    using System;
using System.Collections.Generic;

using System.Globalization;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

public partial class BetweenDates
{
    [JsonProperty("start_date")]
    public long StartDate { get; set; }

    [JsonProperty("end_date")]
    public long EndDate { get; set; }
}

public partial class BetweenDates
{
    public static BetweenDates FromJson(string json) => JsonConvert.DeserializeObject<BetweenDates>(json, Converter.Settings);
}

public static class Serialize
{
    public static string ToJson(this BetweenDates self) => JsonConvert.SerializeObject(self, Converter.Settings);
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
}
