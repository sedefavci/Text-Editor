import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

/**
 * Reads commands from a specified file and executes them using the TextEditor.
 * Acts as a bridge between the input file and the editor logic.
 * Supported commands: insert, delete, replace, undo, redo.
 */
public class CommandReader {
	/**
	 * The TextEditor instance where operations will be performed.
	 */
	private TextEditor editor;

	/**
	 * The path of the file containing the commands (e.g., "actions.txt").
	 */
	private String filePath;

	/**
	 * Constructor: Initializes the CommandReader with a specific editor and file path.
	 * * @param editor   The TextEditor object to control.
	 * @param filePath The path to the file containing commands.
	 */
	public CommandReader(TextEditor editor,String filePath){
		this.editor=editor;
		this.filePath=filePath;
	}

	/**
	 * Opens the file, reads it line by line, parses the commands,
	 * and executes the corresponding methods in the TextEditor.
	 */
	public void readExecute(){
		try{
			File file=new File(filePath); //file nesnesini oluşturuyoruz
			Scanner sc=new Scanner(file); //file'ı okumak için scanner oluşturuyoruz
			System.out.println("---Reading Command File: "+filePath+"---\n");

			//burada file'ın sonuna gelene kadar satır satır okuyoruz
			while(sc.hasNextLine()){
				String line=sc.nextLine();

				//her işlemden önce komutun kendisi yazıyordu verilen örnekte o yüzden bunu yazıyoruz
				System.out.println(line);

				//boş satırları geçmek için
				if (line.trim().isBlank()) continue;

				//satırı boşluklara göre kelime kelime parçalıyoruz (regex "\\s+" birden fazla boşluğu da tek sayar)
				//örn: "insert Hello 0" -> ["insert", "Hello", "0"]
				String[] parts = line.trim().split("\\s+");

				//dizinin ilk elemanı her zaman komut ismidir.
				//buna göre hangi if bloğuna gireceğine karar verecek
				String command = parts[0];

				//komut türüne göre ilgili metodu çağırıyoruz (Büyük/küçük harf duyarlılığı olmadan)
				if (command.equalsIgnoreCase("insert")) {
					//insert komutunu işlemek için çalışır
					//insert <text> <position> yani insert Hello 0
					//eğer metin boşlukluysa parça sayısı 3'ten fazla olabilir
					if (parts.length >= 3) {
						// "insert , World 5" gibi boşluklu metin geldiğinde, pozisyon verisi dizinin en sonunda yer alır.
						// Bu yüzden dizinin en son elemanının indeks numarasını alıyoruz.
						int lastIndex = parts.length - 1;

						//en sondaki elemanı (String halindeki sayıyı) alıp matematiksel int değerine çeviriyoruz
						//Bu değer ekleme yapılacak pozisyon bilgisidir.
						int position = Integer.parseInt(parts[lastIndex]);

						// Aradaki kelimeleri (parts[1]'den sonuncuya kadar) birleştiriyoruz
						StringBuilder textBuilder = new StringBuilder();

						//Döngüyü 1. indeksten (komuttan hemen sonra) başlatıp, son indekse (pozisyona) gelene kadar çalıştırıyoruz.
						for (int i = 1; i < lastIndex; i++) {
							// O anki kelimeyi birleştiriciye ekliyoruz.
							textBuilder.append(parts[i]);

							// Son kelime değilse araya boşluk koyuyoruz.
							if (i < lastIndex - 1) {
								textBuilder.append(" ");
							}
						}
						// StringBuilder içinde birikmiş olan parçaları tek bir String (yazı) haline getiriyoruz.
						String text = textBuilder.toString();

						// Editör nesnesinin insert metodunu çağırarak, hazırlanan metni belirtilen pozisyona ekliyoruz
						editor.insert(text, position);
					}else {
						System.out.println("Error: Missing arguments for insert command.");
					}

				} else if (command.equalsIgnoreCase("delete")) {
					//delete komutunu işlemek için çalışır
					//delete <position> <length> yani delete 0 5
					//eğer metin boşlukluysa parça sayısı 3'ten fazla olabilir
					if (parts.length >= 3) {
						int position=Integer.parseInt(parts[1]);// parts[1] String ("0") olarak gelir, int'e çeviriyoruz.
						int length=Integer.parseInt(parts[2]);// parts[2] String ("5") olarak gelir, int'e çeviriyoruz.

						//Editor'ın delete metodunu çağırıyoruz
						editor.delete(position,length);
					}else {
						System.out.println("Error: Missing arguments for delete command.");
					}
				} else if (command.equalsIgnoreCase("replace")) {
					//replace komutunu işlemek için çalışır
					//replace <newText> <position> <lengthZ yani replace Java 5 3
					//eğer metin boşlukluysa parça sayısı 4'ten fazla olabilir
					if (parts.length >= 4) {
						//dizinin en son elemanını buluyoruz, çünkü formatta length hep en sondadır.
						int lengthIndex = parts.length - 1;
						//bulduğumuz son elemanı String'den int'e çeviriyoruz
						int length = Integer.parseInt(parts[lengthIndex]);

						//dizinin Ssondan bir önceki elemanını buluyoruz, formatta position oradadır.
						int positionIndex = parts.length - 2;
						//bu elemanı da String'den int'e çeviriyoruz.
						int position = Integer.parseInt(parts[positionIndex]);

						//şimdi metni oluşturacağız. Metin, komuttan sonra (parts[1]) başlar, position sayısına kadar (parts[positionIndex]) devam eder.
						//parçaları birleştirmek için verimli olan StringBuilder kullanıyoruz.
						StringBuilder textBuilder = new StringBuilder();

						//döngüyü 1. indeksten (komuttan sonra) başlatıp, pozisyon indeksine gelene kadar döndürüyoruz.
						for (int i = 1; i < positionIndex; i++) {
							textBuilder.append(parts[i]); //o anki kelimeyi (parçayı) ekliyoruz.

							//Eğer bu eklediğimiz kelime son kelime değilse, arasına orijinal boşluğu geri koyuyoruz.
							//Split işlemi boşlukları yuttuğu için onları manuel eklememiz gerekir.
							if (i < positionIndex - 1) {
								textBuilder.append(" ");
							}
						}
						//StringBuilder içindeki birleşmiş yapıyı normal String'e çeviriyoruz.
						String newText = textBuilder.toString();

						//son olarak editörümüze replace yap diyoruz
						editor.replace(newText, position, length);
					}else {
						System.out.println("Error: Missing arguments for replace command.");
					}
				} else if (command.equalsIgnoreCase("undo")) {
					//parametre geremiyor
					//çünkü geri alınacak işlem bilgisi TextEditor'ün içindeki Stack'te zaten kayıtlı oluyor
					//sadece geri al (undo) emrini vermemiz yeterli
					editor.undo();
				} else if (command.equalsIgnoreCase("redo")) {
					//parametre gerekmiyor burada da
					//çünkü ileri alınacak işlem bilgisi redoStack'te zaten kayıtlı oluyor
					editor.redo();
				} else{
					//eğer okunan command (parts[0]) yukarıdaki if'lerin hiçbiriyle (insert, delete, undo,redo,replace) eşleşmezse buraya düşüyor.
					//dosyada hatalı veya bilinmeyen bir komut yazıldığını gösteriyor
					System.out.println("Unknown command "+command);
				}
			}
			sc.close(); //işimiz bittiği için scanner'ı kapattık
		} catch (FileNotFoundException e){
			//eğer constructorda belirtilen 'filePath' adresinde dosya bulunamazsa bu hata fırlatılır.
			//programın çökmesini engellemek için hatayı yakalayıp kullanıcıya "dosya bulunamadı" diyoruz.
			System.out.println("Error: Cannot find the file "+filePath);

		} catch (NumberFormatException e) {
			//dosyada sayı girilmesi gereken yere (pozisyon veya uzunluk) harf ya da sembol girilirse buraya düşer.
			//programın hata verip kapanması yerine kullanıcıya sayı formatının yanlış olduğunu söylüyoruz.
			System.out.println("Error: Invalid number format in command file. Please check the numbers.");

		} catch (Exception e){
			//burası genel hata yakalayıcı kısmı.
			//file okuma veya parse işlemi sırasında oluşabilecek diğer tüm beklenmedik hataları yakalar.
			//e.getMessage() ile hatanın teknik sebebini yazdırıyoruz.
			System.out.println("Error processing file: "+e.getMessage());
		}
	}
}
