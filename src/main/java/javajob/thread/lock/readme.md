#lock接口
        Lock不是Java语言内置的，synchronized是Java语言的关键字，因此是内置特性。Lock是一个类（接口），通过这个类可以实现同步访问；
        Lock和synchronized有一点非常大的不同，采用synchronized不需要用户去手动释放锁，当synchronized方法或者synchronized代码块执行完之后，
        系统会自动让线程释放对锁的占用；而Lock则必须要用户去手动释放锁，如果没有主动释放锁(或者发生异常也不会释放锁)，就有可能导致出现死锁现象。
## 方法  
   - lock()
   - tryLock()   
        &emsp;&emsp;它表示用来尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false，也就说这个方法无论如何都会立即返回。  
   - tryLock(long time, TimeUnit unit)  
       &emsp;&emsp;tryLock(long time, TimeUnit unit)方法和tryLock()方法是类似的，只不过区别在于这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true
   - lockInterruptibly()    
        &emsp;&emsp;当通过这个方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。也就是说拿不到锁，即中断线程，不让其等待。  
   - unLock()方法是用来释放锁的
## ReentrantLock
    唯一的lock接口实现
## ReadWriteLock
    接口，并没有实现
    读写锁是一种先进的线程锁机制。它能够允许多个线程在同一时间对某特定资源进行读取，但同一时间内只能有一个线程对其进行写入。
## ReentrantReadWriteLock
    ReadWriteLock 接口的实现