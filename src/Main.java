/**
 * The entry point of the application.
 * Initializes the TextEditor and triggers the CommandReader to process the input file.
 */

public class Main{
	public static void main(String[] args) {
		System.out.println("------Text Editor STARTED-----\n");

		//TextEditor nesnesi oluşturuluyor.
		//text'in üzerinde yapılacak tüm işlemleri (insert, delete, undo vb.) yönetmek ve metnin güncel hali ile Undo/Redo stack yapılarını hafızada tutabilmek için bu nesne gereklidir.
		TextEditor textEdit=new TextEditor();

		//"actions.txt" dosyasını okuyacak nesne oluşturuluyor.
		//TextEditor nesnesini dışarıdan gelen komutlarla yönetebilmek için dosya okuma işlemini yapan CommandReader sınıfını başlatıyoruz.
		CommandReader commandRead= new CommandReader(textEdit,"actions.txt");

		// Dosya okuma ve komutları işleme süreci başlatılıyor.
		commandRead.readExecute();

		System.out.println("-----Text Editor FINISHED-----");
	}
}