package javajob.other.lombok;

public class Person_test {
    public static void main(String[] args) {
        OP2();
    }
    public static void OP2(){
        Person2 person2 = new Person2();
        Person2 person21 = new Person2();
        System.out.println(person2);
        System.out.println(person2==person21);
    }

    public static void OP(){
        Person person = new Person();
        System.out.println(person);
    }

    public static void OP3(){
        Person3 person3 = new Person3("kingcall", 23, true);
        Person3 person31 = new Person3();
        System.out.println(person3);
        System.out.println(person31);
    }
}
