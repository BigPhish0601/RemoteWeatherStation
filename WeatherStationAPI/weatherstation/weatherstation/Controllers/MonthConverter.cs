using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace weatherstation.Controllers
{
    public class MonthConverter
    {
        public string Month(string m)
        {
           if (m.Contains("Jan")) {
                return "01";
            }
           else if (m.Contains("Feb"))
            {
                return "02";
            }
            else if (m.Contains("Mar"))
            {
                return "03";
            }
            else if (m.Contains("Apr"))
            {
                return "04";
            }
            else if (m.Contains("May"))
            {
                return "05";
            }
            else if (m.Contains("Jun"))
            {
                return "06";
            }
            else if (m.Contains("Jul"))
            {
                return "07";
            }
            else if (m.Contains("Aug"))
            {
                return "08";
            }
            else if (m.Contains("Sep"))
            {
                return "09";
            }
            else if (m.Contains("Oct"))
            {
                return "10";
            }
            else if (m.Contains("Nov"))
            {
                return "11";
            }
            else if (m.Contains("Dec"))
            {
                return "12";
            }
           else
            {
                return "-1";
            }
        }
    }
}
