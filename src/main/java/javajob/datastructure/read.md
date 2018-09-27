#list  
   - ArrayList, LinkedList, Vector, Stack是List的4个实现类  
        - ArrayList 是一个数组队列，相当于动态数组。它由数组实现，随机访问效率高，随机插入、随机删除效率低。  
        - LinkedList 是一个双向链表。它也可以被当作堆栈、队列或双端队列进行操作。LinkedList随机访问效率低，但随机插入、随机删除效率低。  
        - Vector 是矢量队列，和ArrayList一样，它也是一个动态数组，由数组实现。但是ArrayList是非线程安全的，而Vector是线程安全的。  
        - Stack 是栈，它继承于Vector。它的特性是：先进后出(FILO, First In Last Out)