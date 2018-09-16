package javajob.serializable;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *  1.  在序列化过程中，虚拟机会试图调用对象类里的 writeObject 和 readObject 方法，进行用户自定义的序列化和反序列化，如果没有这样的方法，
 *      则默认调用是 ObjectOutputStream 的 defaultWriteObject 方法以及 ObjectInputStream 的 defaultReadObject 方法。基于这个原理，可以在实际应用中得到使用，用于敏感字段的加密工作
 */
public class Password implements Serializable{
    private String password;
    private String pass="kingcall";

    public Password(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 不论序列化，还是反序列化的过程，都是先读取了对象流
     * @param out
     */
    private void writeObject(ObjectOutputStream out) {
        try {
            ObjectOutputStream.PutField putFields = out.putFields();
            System.out.println("原密码:" + password);
            //将要序列化的字段添加了进去，所以在这里可以决定哪些字段可以被序列化
            putFields.put("password", password+":encryption");
            putFields.put("pass", pass);
            System.out.println("加密后的密码" + password+":encryption");
            out.writeFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream in) {
        try {
            ObjectInputStream.GetField readFields = in.readFields();
            // 这里决定要反序列化哪些对象（当然是全部，否则不会序列化出来的）
            Object object = readFields.get("password", "");
            System.out.println("要解密的字符串:" + object.toString());
            //这里相当于设置对象的字段
            password = object.toString().split(":")[0];
            pass=readFields.get("pass","").toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Password{" +
                "password='" + password + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
