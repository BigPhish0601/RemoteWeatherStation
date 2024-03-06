using Npgsql;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Configuration.Assemblies;
using System.Linq;
using System.Web;
using System.Collections.Specialized;

namespace weatherstation.Controllers
{
    public class DatabaseConnection
    {
        public static string GetRDSConnectString()
        {
            return null;
        }
        public List<WeatherDataDto> ReturnBetweenDates(string query)
        {
            
            //var connString = "Host=weatherstationdb.cnownaizmzoh.us-east-1.rds.amazonaws.com;Port=5432;Username=fgcu;Password=fgcu2018;Database=weatherstationdb";
            var connString = "Host=elmer.db.elephantsql.com;Port=5432;Username=nirdrutg;Password=MECexkudXMszDPIlkdJcVH10ONoAStjC;Database=nirdrutg";
            List<WeatherDataDto> dataList = new List<WeatherDataDto>();

            using (var conn = new NpgsqlConnection(connString))
            {
                conn.Open();

                // Retrieve all rows
                using (var humidity_cmd = new NpgsqlCommand(query, conn))
                using (NpgsqlDataReader reader = humidity_cmd.ExecuteReader())
                    while (reader.Read())
                    {
                        WeatherDataDto a = new WeatherDataDto();

                        a.DateTime = Convert.ToInt64(reader.GetValue(3));
                        a.Humidity = Convert.ToDouble(reader.GetValue(1));
                        a.TempInF = Convert.ToDouble(reader.GetValue(2));

                        dataList.Add(a);

                    }
                dataList.Reverse();
                return dataList;
            }
        }
        public void StoreData(string query)
        {
            var connString = "Host=elmer.db.elephantsql.com;Port=5432;Username=nirdrutg;Password=MECexkudXMszDPIlkdJcVH10ONoAStjC;Database=nirdrutg";

            using (var conn = new NpgsqlConnection(connString))
            {
                conn.Open();

                NpgsqlCommand cmd = new NpgsqlCommand(query, conn);
                cmd.ExecuteNonQuery();
                
            }
        }
    }
}
