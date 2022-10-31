using System.Text.RegularExpressions;

namespace RabbitGameServer.SharedModel
{
	public class Color
	{

		private static double MAX_VALUE = 255;

		private static string match_7 = "[a-fA-F0-9]{7}";
		private static string match_6 = "[a-fA-F0-9]{6}";
		private static string match_4 = "[a-fA-F0-9]{4}";
		private static string match_3 = "[a-fA-F0-9]{3}";

		private static string pattern = $"^#({match_7}|{match_6}|{match_4}|{match_3})$";

		private static int SHORT_FORMAT_LEN = 4;
		private static int SHORT_OP_FORMAT_LEN = 5;

		private static int LONG_FORMAT_LEN = 7;
		private static int LONG_OP_FORMAT_LEN = 8;

		public static Color RED = Color.parse("#f00");
		public static Color GREEN = Color.parse("#0f0");
		public static Color BLUE = Color.parse("#00f");

		public double red { get; set; }
		public double green { get; set; }
		public double blue { get; set; }

		public double opacity { get; set; }

		public Color(double red, double green, double blue, double opacity)
		{
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.opacity = opacity;
		}

		public static Color parse(string text)
		{
			var result = Regex.Match(text, pattern);

			if (!result.Success)
			{
				Console.WriteLine("Invalid color format ... ");
				Console.WriteLine("Expected #123 or #123456 but received: " + text);
				Console.WriteLine("Returning default color (RED) ... ");

				return Color.RED;
			}

			if (text.Length == SHORT_FORMAT_LEN)
			{
				var r_value = scale(parseHex(text.Substring(1, 1)));
				var g_value = scale(parseHex(text.Substring(2, 1)));
				var b_value = scale(parseHex(text.Substring(3, 1)));

				return new Color(r_value, g_value, b_value, 1);
			}
			else if (text.Length == SHORT_OP_FORMAT_LEN)
			{
				var r_value = scale(parseHex(text.Substring(1, 1)));
				var g_value = scale(parseHex(text.Substring(2, 1)));
				var b_value = scale(parseHex(text.Substring(3, 1)));

				var op_value = scale(parseHex(text.Substring(4, 1)));

				return new Color(r_value, g_value, b_value, op_value);
			}
			else if (text.Length == LONG_FORMAT_LEN)
			{
				var r_value = scale(parseHex(text.Substring(1, 2)));
				var g_value = scale(parseHex(text.Substring(3, 2)));
				var b_value = scale(parseHex(text.Substring(5, 2)));

				return new Color(r_value, g_value, b_value, 1);
			}
			else if (text.Length == SHORT_OP_FORMAT_LEN)
			{
				var r_value = scale(parseHex(text.Substring(1, 2)));
				var g_value = scale(parseHex(text.Substring(3, 2)));
				var b_value = scale(parseHex(text.Substring(5, 2)));

				var op_value = scale(parseHex(text.Substring(7, 1)));

				return new Color(r_value, g_value, b_value, op_value);
			}

			Console.WriteLine("Color parsing failed (DEFAULT COLOR IS RED) ... ");
			return Color.RED;

		}

		private static int parseHex(string hexValue)
		{
			return int.Parse(hexValue, System.Globalization.NumberStyles.HexNumber);
		}

		private static double scale(int value)
		{
			return (value / 255);
		}

	}
}