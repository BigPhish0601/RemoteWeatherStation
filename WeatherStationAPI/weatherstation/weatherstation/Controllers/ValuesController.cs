using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;


namespace weatherstation.Controllers
{
    [Route("api/[controller]")]
    public class ValuesController : Controller
    {
        DatabaseConnection db = new DatabaseConnection();
        MonthConverter mc = new MonthConverter();


        // POST api/values
        [Route("insertweatherdata")]
        [HttpPost]
        [Consumes("application/json")]
        [Produces("text/plain")]
        public string InsertData([FromBody] Object value)
        {

            try
            {

                var weatherDataDto = WeatherDataDto.FromJson(value.ToString());

                double date = weatherDataDto.DateTime;
                double humidity = Convert.ToDouble(weatherDataDto.Humidity);
                double temp_in_f = Convert.ToDouble(weatherDataDto.TempInF);

                //string objectValue = value.ToString();
                //string date = objectValue.Substring(19, 19);
                //string humidity = objectValue.Substring(57, 5);
                //string temp = objectValue.Substring(82, 5);

                //CHECK ALL VALUES
                string sqlQuery = "INSERT INTO weatherdata (date_time, humidity, temp_in_f) VALUES(" + date + ", " + humidity + ", " + temp_in_f + ");";

                //INSERT VALUES INTO DATABASE
                db.StoreData(sqlQuery);
                return "Data got here: " + value.ToString();

            }
            catch (Exception e)
            {
                return "something went wrong";

            }

        }


        // POST api/values
        [Route("betweendates")]
        [HttpPost]
        [Consumes("application/json")]
        [Produces("text/plain")]
        public string Dates([FromBody] Object value)
        {

            var betweenDateDTO = BetweenDates.FromJson(value.ToString());
            long start_date_in_milliseconds = betweenDateDTO.StartDate;
            long end_date_in_milliseconds = betweenDateDTO.EndDate;


            //BetweenDates dateResult = JsonConvert.DeserializeObject<BetweenDates>(value.ToString());

            //string startDate = dateResult.Start_date.ToString();
            //string temp = startDate.Substring(4, 3);
            //string startMonth = mc.Month(startDate.Substring(4, 3));
            //string startday = startDate.Substring(8, 2);
            //string startYear = startDate.Substring(24, 4);
            //string sDate = startYear + "-" + startMonth + "-" + startday;

            //string endDate = dateResult.End_date.ToString();
            //string endMonth = mc.Month(endDate.Substring(4, 3));
            //string endDay = endDate.Substring(8, 2);
            //string endYear = endDate.Substring(24, 4);
            //string eDate = endYear + "-" + endMonth + "-" + endDay;

            //string sqlQuery = "SELECT * FROM weatherdata WHERE date_time >= '" +
            //            sDate + "' AND date_time <= '" + eDate + "';";

            string sqlQuery = $"select * from weatherdata where date_time >= {start_date_in_milliseconds} and date_time <= {end_date_in_milliseconds}";

            var fuckyeah = db.ReturnBetweenDates(sqlQuery);

            string json = JsonConvert.SerializeObject(fuckyeah);

            //Created new sql statements with appropriate dates
            //execute queries
            //return values here
            return json;

        }

        // GET api/values
        [HttpGet]
        public IEnumerable<string> Get()
        {
            return new string[] { "value1", "value2" };
        }

    }
}
