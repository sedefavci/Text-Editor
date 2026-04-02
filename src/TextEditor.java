import java.util.Stack;

/**
 * Represents a simple text editor with Undo/Redo functionality.
 * Supports basic operations like insert, delete, and replace.
 * * Uses StringBuilder for efficient text manipulation and Stacks
 * to maintain the history of actions for undo/redo capabilities.
 */
public class TextEditor {
	/**
	 * The main text container. StringBuilder is used for efficient string manipulation
	 * because strings in Java are immutable, but StringBuilder is mutable.
	 */
	private StringBuilder currentText; //Sadece "Böyle bir değişkenim olacak" diyoruz

	/**
	 * Stack to store executed actions. This is used to reverse (undo) operations.
	 */
	private Stack<Action> undoStack;

	/**
	 * Stack to store undone actions. This is used to re-apply (redo) operations.
	 */
	private Stack<Action> redoStack;

	/**
	 * Constructor: Initializes the editor with empty text and empty stacks.
	 */
	public TextEditor(){
		// StringBuilder'ı boş bir metin olarak başlatıyoruz
		this.currentText=new StringBuilder(); //eğer bunu yapmazsan NullPointerException hatası alırız

		//geri alma ve yineleme geçmişlerini tutacak boş yığınları (Stack) oluşturuyoruz.
		this.undoStack=new Stack<>(); //geri alma geçmişi boş
		this.redoStack=new Stack<>(); //yineleme geçmişi boş
	}
	//Editör her zaman boş bir defter olarak açılır bu yüzden üstteki gibi bir constructor yeterli diye başka constuctor yazmadık

	/**
	 * Inserts text at the specified position.
	 * @param text The text to be inserted.
	 * @param position The index where the text will be inserted.
	 */
	public void insert(String text, int position){
		//sınır kontrol ediliyor-pozisyon negatif olamaz ve mevcut metnin boyunu aşamaz
		//eğer girilen pozisyon hatalıysa işlem iptal edilir ve kullanıcı uyarılır
		if (position<0 || position>currentText.length()) {
			System.out.println("Error: Invalid position " + position); //hata olduğu için metoddan çıkılıyor, işlem yapılmıyor
			return;
		}
		//StringBuilder kütüphanesinin insert metodu kullanılarak ekleme yapılıyor
		currentText.insert(position,text); //işlem gerçekleştiriliyor

		//yapılan işlem geri alınabilsin diye Action nesnesi olarak paketleniyor
		Action action = new Action("insert",text,position);//geçmişe kaydediliyor(Undo için)

		//oluşturulan bu işlem geçmiş yığınına (stack) atılıyor
		undoStack.push(action);//action kaydediliyor

		//yeni bir işlem yapıldığında eski ileri alma (redo) zinciri kopar, bu yüzden temizlenir
		redoStack.clear();//geleceği temizliyoruz çünkü redo stack artık geçersiz

		//outputta çıkması istenenler
		System.out.println("Operation: \""+text +"\" was inserted at position "+position );
		System.out.println("Text: "+currentText.toString() +"\n");
	}

	/**
	 * Deletes a segment of text starting from a given position with a specified length.
	 * The deleted text is saved as an Action for the Undo operation.
	 *
	 * @param position The starting index for deletion.
	 * @param length The number of characters to delete.
	 */
	public void delete(int position,int length){
		//sınır kontrol ediliyor. Position, length negatif olamaz ve silinecek alan textin dışına taşamaz.
		//eğer limitler aşılırsa kullanıcı uyarılır ve işlem yapılmaz
		if(position <0||length<0||position+length>currentText.length()){
			System.out.println("Error:Invalid position or length.");
			return; //hata olduğu için metoddan çıkılıyor
		}
		//silinecek texti burada yedeğe alıyoruz çünkü undo işlemi için bize lazım
		//silme işlemi yapıldıktan sonra bu veriye ulaşamayız o yüzden önce saklıyoruz
		String deletedText=currentText.substring(position,length+position); //başlangıç ve bitiş gerkli substring için. örn:5'ten başla, 5+3=8'e kadar git

		//asıl silme işlemi-delete methodu ile
		//StringBuilder kütüphanesinin delete fonksiyonu kullanılıyor
		currentText.delete(position,position+length);//delete metodu da başlangıç ve bitiş ister

		//geçmişe kayıt-stack işlemleri
		//silinen parça ve pozisyon bilgisi Action objesine yükleniyor
		Action action=new Action("delete",deletedText,position);
		undoStack.push(action);//action burada kaydediliyor

		//yeni bir silme yapıldığı için ileri alma (redo) ihtimali kalmaz
		redoStack.clear(); //yeni işlem yapıldığı için redo sıfırlıyoruz

		//outputta çıkması istenenler
		//işlem sonucu ve metnin son durumu ekrana basılıyor
		System.out.println("Operation: "+length+" characters starting from position " +position+" (\"" +deletedText+"\") were deleted.");
		System.out.println("Text: "+currentText.toString() +"\n");
	}

	/**
	 * Replaces a segment of text with new text.
	 * The old text and new text are stored in the Action object for undo/redo.
	 *
	 * @param newText The text that will be inserted.
	 *                Karışıklık olmasın, yeni olduğu belli olsun diye text yerine newText.
	 *                Amaç, okunabilirliği arttırmak.
	 * @param position The starting index for replacement.
	 * @param length The number of characters to replace.
	 */
	public void replace(String newText, int position, int length){
		//sınır kontrol ediliyor. Position, length negatif olamaz ve silinecek alan textin dışına taşamaz.
		//eğer bu şartlar sağlanmazsa hata mesajı verip işlemden çıkıyoruz
		if (position <0||length<0||position+length>currentText.length()) {
			System.out.println("Error:Invalid position or length.");
			return;
		}
		//silinecek "old text"i kaydediyoruz undo için
		//çünkü replace işlemi geri alınırken (undo) eski metni yerine koymamız gerekecek
		String oldText=currentText.substring(position,position+length);

		//stringbuilder'ın replace metodunu kullanarak belirtilen aralığı yeni metinle değiştiriyoruz
		currentText.replace(position,position+length,newText); //currentText.replace metodu, (Başlangıç indeksi, Bitiş indeksi, Yeni metin) ister.

		//geçmişe kayıt kısmı-Action'ın ikinci constructor'ını kullanıyoruz
		//action'ın 2. constructor'ındaki gibi hem yeni metni hem de eski metni saklıyoruz ki dönüşte ikisi de elimizde olsun
		Action action=new Action("replace",newText,oldText,position);
		undoStack.push(action); //yığın hafızasına (stack) atıyoruz
		redoStack.clear(); //yeni işlem gelince redo geçmişi silinir

		//outputta çıkması istenenler
		//işlemin sonucunu ve metnin son halini ekrana basıyoruz
		System.out.println("Operation: "+length +" characters starting from position "+position +" (\""+oldText+"\") were replaced with \""+newText+"\"");
		System.out.println("Text: "+currentText.toString() +"\n");
	}

	/**
	 * Undoes the last operation.
	 * Moves the action from Undo Stack to Redo Stack.
	 */
	public void undo(){
		//Stack boş mu kontrol ediyoruz
		//eğer geri alınacak işlem yoksa uyarı verip çıkıyoruz
		if (undoStack.isEmpty()){
			System.out.println("There is no text/action to undo ");
			return;
		}
		//Redo'dan al, Undo'ya geri koy
		//çünkü redo yapılan işlem tekrar "yapılmış" sayılır ve tekrar geri alınabilir olmalıdır
		Action action=undoStack.pop();//son işlem(last action)
		redoStack.push(action); //geri aldığımız şeyi ileride tekrar yapmak isteyebiliriz o yüzden kaydediyoruz

		//Action'ın type'ına bakacağız ki tersini yapabilelim
		String type=action.getType();

		//ayrıca bu ikisini(position,text) kodun devamında yani if-else kısımlarını yazarken kolaylık olsun diye tanımladım
		//bunları tanımladığımız içn artık action.getPosition() ve action.getText() yazmamıza gerek kalmayacak
		int position=action.getPosition();
		String text=action.getText();

		//undo() methodunun parametresi olmadığı için-undoStack'teki action ile, bu işlemde eklenen metin için "action.getText()".uzunluk için "action.getText().length()"demeliyiz.
		//bu yüzden diğer methodlarda yaptığımız gibi direkt position,length... çağıramıyoruz
		if(type.equals("insert")){
			//insert yapılmış, undo edince delete olur
			//silinecek miktar, eklenen kelimenin uzunluğu kadar olur
			currentText.delete(position,position+text.length()); //getPosition var ama getLength yok o yüzden text.length() yaptık
			System.out.println("Operation: The last insert operation was undone. The inserted text \"" +text+ "\" was removed.");
		} else if (type.equals("delete")) {
			//delete yapılmış, undo edince tam tersi insert olur
			//silinen metin action.getText() içinde saklıydı, onu geri koyuyoruz
			currentText.insert(position,text); //Action içinde saklanırken adı: text (Action sınıfı genel olduğu için).Undo yaparken adı: text (Action'dan öyle geldiği için).Dışarıdan gelirken adı: newText (Karışıklık olmasın, yeni olduğu belli olsun diye).
			System.out.println("Operation: The last delete operation was undone. The deleted text \"" +text + "\" was restored.");
		} else if (type.equals("replace")) {
			// replace yapılmış, undo edince eski texti koyuyoruz
			//orada "new text" (action.getText()) duruyor. Onu bulup siliyoruz
			//yerine "eski metni" (action.getOldText()) koyuyoruz
			currentText.replace(position,position+text.length(),action.getOldText());
			System.out.println("Operation: The replace operation was undone. \"" +text + "\" was removed and \"" + action.getOldText() + "\" was restored.");
		}else{
			//bilinmeyen bir işlem tipi gelirse uyarı veriyoruz
			System.out.println("Unknown action type!");
		}
		//her işlemden sonra metnin güncel halini yazdırıyoruz
		System.out.println("Text: " + currentText.toString() +"\n");
	}

	/**
	 * Redoes the last undone operation.
	 * Moves the action from Redo Stack to Undo Stack.
	 */
	public void redo(){
		//Stack boş mu kontrol ediyoruz
		//yapılacak redo işlemi yoksa uyarı verip çıkıyoruz
		if(redoStack.isEmpty()){
			System.out.println("There is no text/action that can redo ");
			return;
		}
		//Redo'dan al, Undo'ya geri koy
		//çünkü redo yapılan işlem tekrar "yapılmış" sayılır ve tekrar geri alınabilir olmalıdır
		Action action=redoStack.pop();
		undoStack.push(action);

		//bunları tanımladığımız içn artık action.getType(),action.getPosition() ve action.getText() yazmamıza gerek kalmayacak
		String type=action.getType();
		int position=action.getPosition();
		String text=action.getText();

		if(type.equals("insert")){
			//silinmişti, tekrar geri ekliyoruz
			//undo işlemi insert'ü silmişti, redo işlemi tekrar ekler
			currentText.insert(position,text);
			System.out.println("Operation: The last insert operation was redone. The inserted text \"" +text+ "\" was restored again.");
		}else if (type.equals("delete")){
			//geri gelmişti, tekrar siliyoruz
			//undo işlemi silineni geri getirmişti, redo işlemi onu tekrar siler
			currentText.delete(position,position+text.length());//silincek miktar metnin kendi uzunluğu
			System.out.println("Operation: The last delete operation was redone. The deleted text \"" +text + "\" was deleted again.");
		} else if (type.equals("replace")) {
			//eski text gelmişti omu silip yeni texti koyuyoruz
			//ekranda şu an 'oldText' duruyor. O yüzden oldText'in uzunluğu kadar length'i seçip değiştiriyoruz
			currentText.replace(position,position+action.getOldText().length(),text);
			System.out.println("Operation: The replace operation was redone. \"" + action.getOldText()+ "\" was removed again and replaced with \"" + text + "\".");
		}
		//metnin son halini yazdırıyoruz
		System.out.println("Text: " + currentText.toString() +"\n");
	}
}
