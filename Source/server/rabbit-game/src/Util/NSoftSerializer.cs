using System.Text;
using Newtonsoft.Json;

namespace RabbitGameServer.Util
{
	public class NSoftSerializer : ISerializer
	{
		public byte[] ToBytes(Object obj)
		{
			var strContent = this.ToString(obj);
			return Encoding.UTF8.GetBytes(strContent);
		}

		public string ToString(Object obj)
		{
			return JsonConvert.SerializeObject(obj);
		}

		public Object ToObj(byte[] bytes, Type type)
		{
			var strContent = Encoding.UTF8.GetString(bytes);
			return this.ToObj(strContent, type);
		}

		public Object ToObj(string text, Type type)
		{
			return JsonConvert.DeserializeObject(text, type);
		}

		public T ToObj<T>(byte[] bytes) where T : class
		{
			var strContent = Encoding.UTF8.GetString(bytes);
			return this.ToObj<T>(strContent);
		}

		public T ToObj<T>(string text) where T : class
		{
			return JsonConvert.DeserializeObject<T>(text);
		}
	}
}