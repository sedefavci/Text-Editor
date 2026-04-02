/**
 * Action Class.
 *
 * This class represents a single atomic operation (insert, delete, or replace) performed
 * in the text editor.
 * To implement "Undo" and "Redo", we need to store a history of exactly what changed.
 * This class acts as a data object that records the "type" of action, "where" it happened,
 * and "what data" was involved.
 */
public class Action {
	/**
	 * The type of the action.
	 * Expected values: "insert", "delete", "replace".
	 * This determines how the Undo/Redo logic will process this object.
	 */

	// INSTANCE VARIABLES

	private String type;
	/**
	 * The starting index (position) where the action occurred in the text.
	 * Example: If we insert text at index 5, this value is 5.
	 */
	private int position;
	/**
	 * The main text involved in the operation.
	 * For insert-the text that was added.
	 * For delete-the text that was removed (saved here to restore it later).
	 * For replace The NEW text that replaced the old one.
	 */
	private String text;
	/**
	 * The original text before a replacement occurred.
	 * This is ONLY used for "replace" actions.
	 * Sadece replace için, diğerlerinde null kalabilir
	 * We need to store the old text so we can put it back during an Undo.
	 */
	private String oldText;

	//CONSTRUCTORS

	/**
	 * Constructor 1: For insert and delete operations.
	 * These operations only deal with one piece of text (either added or removed).
	 * delete yaparken silinen text'i buraya 'text' olarak göndereceğiz
	 *
	 * @param type     The type of action ("insert" ya da "delete").
	 * @param text     The text that was inserted or deleted.
	 * @param position The position where the action took place.
	 */
	public Action(String type, String text, int position) {
		this.type = type;
		this.text = text;
		this.position = position;
	}

	/**
	 * Constructor 2: Specifically for Replace operations.
	 * For a 'replace', we need to know both what was removed (oldText) and what was added (text).
	 * Hem yeni hem eski texte ihtiyaç var
	 *
	 * @param type     The type of action (usually "replace").
	 * @param text     The new text.
	 * @param oldText  The old text that was replaced (needed for undo).
	 * @param position The position where the replacement started.
	 */
	public Action(String type, String text, String oldText, int position) {
		this.type = type;
		this.text = text;        //yeni metin örn:TextEditor classında bunun için kullanılacak
		this.oldText = oldText;  //eski metin
		this.position = position;
	}

	//GETTER VE SETTER METHODLARI

	/**
	 * Retrieves the type of the action performed.
	 * @return The type of the action (e.g., "insert", "delete", "replace").
	 */
	public String getType(){return type;}

	/**
	 * Retrieves the starting position (index) where the action occurred.
	 * @return The index representing the position in the text.
	 */
	public int getPosition(){return position;}

	/**
	 * Retrieves the main text associated with the action.
	 * Depending on the action type, this could be the inserted text,
	 * the deleted text, or the new text replacing the old one.
	 * @return The text content involved in the action.
	 */
	public String getText(){return text;}

	/**
	 * Retrieves the old text that was replaced during a "replace" action.
	 * This value is essential for restoring the original state during an undo operation.
	 * @return The original text before the replacement occurred.
	 */
	public String getOldText(){return oldText;}

	/**
	 * Sets the type of the action.
	 * @param type The new type of the action to be set.
	 */
	public void setType(String type) {this.type = type;}

	/**
	 * Sets the main text associated with the action.
	 * @param text The new text content to be stored.
	 */
	public void setText(String text) {this.text = text;}

	/**
	 * Sets the starting position (index) of the action.
	 * @param position The new position index to be set.
	 */
	public void setPosition(int position) {this.position = position;}
	/**
	 * Sets the old text for a "replace" action.
	 * @param oldText The original text to be stored for undo purposes.
	 */
	public void setOldText(String oldText){this.oldText=oldText;}
}