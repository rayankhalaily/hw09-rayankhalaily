public class List {
    private Node first;
    private int size;

    public List() {
        first = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public void addFirst(char chr) {
        Node newNode = new Node(new CharData(chr));
        newNode.next = first;
        first = newNode;
        size++;
    }

    public int indexOf(char chr) {
        Node current = first;
        int i = 0;
        while (current != null) {
            if (current.cp.chr == chr) { // התיקון הקריטי כאן
                return i;
            }
            current = current.next;
            i++;
        }
        return -1;
    }

    public void update(char chr) {
        int index = indexOf(chr);
        if (index == -1) {
            addFirst(chr);
        } else {
            get(index).count++;
        }
    }

    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    }

    public boolean remove(char chr) {
        Node prev = null;
        Node current = first;
        while (current != null && current.cp.chr != chr) {
            prev = current;
            current = current.next;
        }
        if (current == null) return false;
        if (prev == null) first = current.next;
        else prev.next = current.next;
        size--;
        return true;
    }

    public String toString() {
        if (size == 0) return "()";
        StringBuilder str = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            str.append(current.cp + " ");
            current = current.next;
        }
        str.deleteCharAt(str.length() - 1);
        str.append(")");
        return str.toString();
    }
}
