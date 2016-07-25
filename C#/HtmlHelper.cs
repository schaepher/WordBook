using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;

namespace NetAccess
{
    class HtmlHelper
    {
        private static WebClient client = null;
        private static string baseUrl = @"https://translate.google.com.hk/translate_a/single?client=t&sl=en&tl=zh-CN&hl=en&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&otf=1&rom=0&ssel=0&tsel=0&pc=1&kc=2&tk=864141.726718&q=TOBEREPLACED";

        public static List<string> Parse(string htmlContent)
        {
            List<string> resultList = new List<string>();

            // 单词
            string regWordStr = "http://www.collinsdictionary.com/dictionary/english/(.+?)\"";
            Regex regWord = new Regex(regWordStr, RegexOptions.Singleline);
            Match word = regWord.Match(htmlContent);
            string wordStr = word.Groups[1].Value;
            resultList.Add(wordStr);

            // 发音
            string regPronStr = "<span class=\" pron\".+?>(.+?)</?span";
            Regex regPron = new Regex(regPronStr, RegexOptions.Singleline);
            Match pron = regPron.Match(htmlContent);
            string pronStr = pron.Groups[1].Value;
            resultList.Add(pronStr);

            // 所有解释
            string regBlockStr = "<div class=\"dictname\">COBUILD Advanced British English Dictionary</div>"
                + "(.+?)<div class=\"content-box";
            Regex regBlock = new Regex(regBlockStr, RegexOptions.Singleline);
            Match block = regBlock.Match(htmlContent);
            string blockStr = block.Groups[1].Value;

            // 替换掉解释里当前单词独特标志
            string regDelHiStr = "<span class=\" hi\" rend=\"b\">(.+?)</span>";
            Regex regDelHi = new Regex(regDelHiStr, RegexOptions.Singleline);
            string blockStrDelHi = regDelHi.Replace(blockStr, "{$1}");

            // 替换掉解释里的剩余标签
            string regDelStr = "<.+?>";
            Regex regDel = new Regex(regDelStr, RegexOptions.Singleline);

            // 获取单个解释块
            string regDefStr = "<span class=\" def\">(.+?\\.)</span>(.+?)</div>";
            Regex regDef = new Regex(regDefStr, RegexOptions.Singleline);

            // 获取例句
            string regExmpStr = "<span> ⇒ </span>(.+?)</span>";
            Regex regExmp = new Regex(regExmpStr, RegexOptions.Singleline);

            MatchCollection defiences = regDef.Matches(blockStrDelHi);
            foreach (Match def in defiences)
            {
                StringBuilder sb = new StringBuilder();
                sb.AppendLine(regDel.Replace(def.Groups[1].Value, ""));

                MatchCollection exmps = regExmp.Matches(def.Groups[2].Value);

                resultList.Add(sb.ToString());
                sb.Clear();
                foreach (Match exmp in exmps)
                {
                    sb.AppendLine(regDel.Replace(exmp.Groups[1].Value, ""));
                }
                resultList.Add(sb.ToString());

            }

            return resultList;
        }

        public static string Translate(string toBeTranslate)
        {
            int index = toBeTranslate.IndexOf('.');
            string temp = toBeTranslate.Remove(index);
            string escapeUri = Uri.EscapeUriString(temp);
            string url = baseUrl.Replace("TOBEREPLACED", escapeUri);
            string result = ReadHtml(url);
            Regex reg = new Regex(@"\[\[\[""(.+?)""", RegexOptions.Singleline);
            Match match = reg.Match(result);
            result = match.Groups[1].Value;
            return result;
        }

        public static string ReadHtml(string url)
        {
            if (client == null)
            {
                client = new WebClient();
                client.Headers.Add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.0.3705;)");
            }

            bool retry = false;
            do
            {

                try
                {
                    Stream data = client.OpenRead(url);
                    StreamReader reader = new StreamReader(data);
                    string content = reader.ReadToEnd();
                    reader.Close();
                    data.Close();
                    return content;
                }
                catch (Exception e)
                {
                    // 只重试一次
                    if (retry == true)
                    {
                        retry = false;
                    }
                    else
                    {
                        retry = true;
                    }
                }

            } while (retry);

            return "";
        }


    }  // Class End
}
