import java.util.LinkedList;

public class LLStack<T> {
    private LinkedList<T> stack;
    public LLStack() {
        stack = new LinkedList<>();
    }

    public void push(T item) {
        stack.addFirst(item);
    }

    public T pop() {
        return stack.removeFirst();
    }

    public T peek(){
        return stack.peekFirst();
    }

    public static void main(String[] args) {
        LLStack<Integer> myStack = new LLStack<>();
        myStack.push(1);
        myStack.push(2);
        myStack.push(3);
        System.out.println(myStack.peek());
        myStack.pop();
        myStack.pop();
        System.out.println(myStack.peek());
    }
}
