import java.util.*;

/**
 *author:liutao19970208@gmail.com
 *date:2022/6/25
 *description:自定义一个集合
*/

public class MyArrayList implements List {
  //保存数据的容器object
  private Object[] elementData;

  //集合默认初始容量
  private final static int DEFAULT_CAPACITY = 10;

  //当指定了容量
  private final static Object[] EMPTY_ELEMENTDATA = {};

  //未指定容量时，elementData指向该数组
  private final static Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

  //计数变量
  //记录集合中已保存的数据
  private int size;

  //记录集合发生改变的次数
  private int modCount;

  //1、当不想指定初始容量，使用默认
  public MyArrayList() {
    elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
  }

  //2、指定初始容量
  public MyArrayList(int initialCapacity) {
    if (initialCapacity == 0) {
      elementData = EMPTY_ELEMENTDATA;  //指定长度
    }else if (initialCapacity > 0) {
      this.elementData = new Object[initialCapacity];
    }else {
      //使用抛出异常提醒用户操作方式不正确
      throw new IllegalArgumentException("Illegal Capacity: "
              + initialCapacity);
    }
  }

  /**
   *向集合尾部添加一个元素
   *return表示插入是否成功（元素数量是否发  生了改变）
   */
  @Override
  public boolean add(Object o) {
  //检查容量
  ensureCapacity(size + 1);

  //将新元素插入到数组size位置
  elementData[size++] = o;
  modCount++;
  return true;
  }

  /**
   * 确保容量够用方法
   * minCapacity 本次扩容需要的最小容量
   */
  private void ensureCapacity(int minCapacity) {
    //先判断当前数组是否等于DEFAULTCAPACITY_EMPTY_ELEMENTDATA
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
      int capacity = Math.max(DEFAULT_CAPACITY, minCapacity);
      elementData = new Object[capacity];
      return;
    }

    if (size >= elementData.length) {
      //获取当前容量
      int oldCapacity = elementData.length;
      //扩容1.5倍，计算
      int newCapacity = oldCapacity + (oldCapacity >> 1);

      /**
       * 计算出新容量后判断是否满足minCapacity的最小需求
       * 满足不了直接使用minCapacity作为扩容长度（初始容量太小；addAll添加元素时要添加的元素过多）
       */
      if (newCapacity < minCapacity) {
        newCapacity = minCapacity;
      }
      //复制为新的数组
      elementData = Arrays.copyOf(elementData, newCapacity);
    }
  }

  /**
   * 删除元素
   * 在数组中无法物理删除，只能逻辑删除
   * 先在数组中找到这个被删元素【确认此元素是否存在于数组】
   * 从被删元素到右边最后，全部元素右移一位
   * @param o
   * @return
   */
  @Override
  public boolean remove(Object o) {
  //在数组中找到这个被删除的元素;如果删除的元素为空
    if (o == null) {
      for (int i = 0; i < size; i++) {
        if (elementData[i] == null) {
          fastRemove(i);
          return true;
        }
      }
    }else {
      //如果删除的元素不为空
      for (int i = 0;i < size; i++) {
        if (o.equals(elementData[i])) {
          fastRemove(i);
          return true;
        }
      }
    }
    return false;
  }

  /**
   *快速删除方法
   *将index+1~index-1范围的元素全部左移一位
   *计算出位移的长度
   */
  private void fastRemove(int index) {

    int numMoved = size - index -1;
    //通过数组复制的方法实现数组内连续元素的转移
    if (numMoved > 0) {
      System.arraycopy(elementData, index + 1,
              elementData, index, numMoved);
    }
    //计数变量对应递减
    elementData[-- size] = null;
  }

  /**
   * 返回集合中元素的数量
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * 清空集合
   */
  @Override
  public void clear() {
    //为加速gc回收，遍历一次集合将所有元素设为null
    for (int i = 0; i < size; i++) {
      elementData[i] = null;
    }
    //如果不考虑性能，仅将size设为0
    size = 0;
  }

  /**
   * 判断集合是否为空
   * @return
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * 判断集合是否包含某个元素
   * @param o
   * @return
   */
  @Override
  public boolean contains(Object o) {
    return indexOf(o) != -1;
  }

  @Override
  public Iterator iterator() {
    return null;
  }

  /**
   * 返回一个包含集合中所有元素的数组
   * *注意：即使当前ArrayList结构本身就是使用一个Object[]数组来实现数据的存储
   * 但是也绝对不能直接把这个数组的引用返回回去。
   * 1、违反了封装原则
   * 内部的实现细节（private属性）不应该对外暴露
   * 如果把elementData引用返回给外界，外界直接可以对其中的元素进行操作
   * 这样就破坏了我们ArrayList自身的功能完整性
   * 2、返回的数组实际长度大于需求长度
   * elementData.length >= size
   * 正确的实现思路是将集合中逻辑插入的所有元素拷贝一份，
   * 返回一个新的数组引用。
   * @return
   */
  @Override
  public Object[] toArray() {
    return Arrays.copyOf(elementData, size);
  }

  /**
   * 查找某个元素第一次出现的位置，若无此元素返回-1
   * @param o
   * @return
   */
  @Override
  public int indexOf(Object o) {
    if (o == null) {
      for (int i = 0; i < size; i++) {
        if (elementData[i] == null) {
          return -1;
        }
      }
    }else {
      for (int i = 0; i < size; i++) {
        if (o.equals(elementData[i])) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * 查找某元素在数组中最后出现的位置
   * @param o
   * @return
   */
  @Override
  public int lastIndexOf(Object o) {
//    //正向遍历（慢，浪费资源）
//    int index = -1;
//    for (int i = 0; i < size; i++) {
//      if (o == elementData[i]) {
//        index = i;
//      }
//    }
//    return index;


    //2、逆向遍历（无需完整遍历集合）
    if(o == null) {
      for (int i = size - 1; i >= 0; i--) {
        if (elementData[i] == null) {
          return i;
        }
      }
    }else {
      for (int i = size - 1; i >= 0; i--) {
        if (o.equals(elementData[i])) {
          return i;
        }
      }
    }
    return -1;
  }
/**************List接口中提供了四种堆元素进行定向增删改查的方法*******************/

/**
 * 下边四个方法共同特点：都需要传入一个index表示要操作的位置
 * 必须要在实际操作之前，对index的值进行判断，看是否在合法范围内。
 * 合法范围：
 * add()：0 ~ size，保证添加的新元素能够和已有的元素保持连接状态
 * remove()/set()/get()：0 ~ size - 1，这三种操作都必须要求访问的位置上是有元素的
 */

  /**
   * 方法1
   * 检查某个索引index是否在0 ~ size - 1范围内
   */
  private void rangeCheck(int index) {
    if (index < 0 || index >= size)
      throw new ArrayIndexOutOfBoundsException("index:" + index);
  }
  /**
   * 方法2
   * 检查某个索引index是否在0 ~ size范围内（给add()方法使用）
   */
  private void rangeCheckForAdd(int index) {
    if (index < 0 || index > size)
      throw new ArrayIndexOutOfBoundsException("index:" + index);
  }

  /**
   * 增加元素
   * @param index
   * @param element
   */
  @Override
  public void add(int index, Object element) {
    //检查元素范围是否在0~size
    rangeCheckForAdd(index);

    //确保容量够用
    ensureCapacity(size + 1);

    //起始位置：index，放置位置：index+1，长度：size-index
    System.arraycopy(elementData, index, elementData,
            index+1, size - index);

    //插入到集合中
    elementData[index] = element;

    size++;
  }

  /**
   * 根据索引删除元素
   * @param index
   * @return 返回的是被删除元素
   */
  @Override
  public Object remove(int index) {
    //检查元素范围是否在0~size-1
    rangeCheck(index);

    //先把被删除元素取出，保存至临时变量oldValue
    Object oldValue = elementData[index];

    //将index+1~size-1的元素左移一位
    int numMoved = size - index - 1;
    if (numMoved > 0) {
      System.arraycopy(elementData, index + 1,
              elementData, index, numMoved);
    }
    //清空最后一个位置元素
    elementData[size --] = null;
    //返回被删除元素
    return oldValue;
  }

  /**
   * 替换指定元素
   * @param index
   * @param element
   * @return 返回的是被替换掉的元素
   */
  @Override
  public Object set(int index, Object element) {
    //检查元素范围是否在0~size-1
    rangeCheck(index);

    //把数组要被替换的元素保存至临时变量
    Object oldValue = elementData[index];

    //将新元素放在指定位置
    elementData[index] = element;

    //返回被替换的元素
    return oldValue;
  }

  /**
   * 查找元素
   * @param index
   * @return
   */
  @Override
  public Object get(int index) {
    //检查元素范围是否在0~size-1
    rangeCheck(index);

    //返回元素
    return elementData[index];
  }

/***************************批量处理方法**************************/
  /**
   * 批量添加元素，将集合c中的元素放到当前元素末位
   * @param c
   * @return 原集合元素数量是否发生了改变
   */
  @Override
  public boolean addAll(Collection c) {
    //将collectionc集合转化为数组
    Object[] newArray = c.toArray();

    //获取要添加的元素数量
    int length = newArray.length;

    //确保容量
    ensureCapacity(size+length);

    //复制数组到当前集合
    System.arraycopy(newArray, 0, elementData, size, length);

    //size递增
    size += length;
    return length != 0;
  }

  /**
   * 批量添加元素到指定位置
   * @param index
   * @param c
   * @return 返回原集合元素数量是否发生了改变
   */
  @Override
  public boolean addAll(int index, Collection c) {
    //检查index取值是否合法
    rangeCheckForAdd(index);

    //将集合转化为数组
    Object[] newArray = c.toArray();

    //获取要添加的元素数量
    int length = newArray.length;

    //确保容量
    ensureCapacity(size + length);

    //将当前数组部分元素右移，起始位置：index，存放位置：index+length，位移长度：size-index
    int numMoved = size - index;
    if (numMoved > 0)
      System.arraycopy(elementData, index, elementData,
              index + length, numMoved);

    //将新数组元素复制到当前数组，起始位置：0，存放位置：index+length，复制长度：length
    System.arraycopy(newArray, 0, elementData, index, length);

    //size递增
    size += length;

    //返回原集合元素数量是否发生了改变
    return length != 0;
  }

  /**
   * 判断当前集合是否包含目标集合所有元素
   * 当集合c中每一个元素在本机和中均满足a.contains（c）时返回true
   * @param c
   * @return
   */
  @Override
  public boolean retainAll(Collection c) {
    //将集合化为数组
    Object[] newArray = c.toArray();

    //遍历数组，判断每个元素在当前集合中是否存在
    for (int i = 0; i < newArray.length; i++) {
      if (!contains(newArray[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * 定义一个批量删除元素功能，求差集使用
   * @return
   */
  private boolean batchRemove(Collection c, boolean rule) {
    /*
     * 移动整理算法
     * 1、准备两根指针，一个叫读指针，一个叫写指针
     * 2、读指针依次指向数组中的每个元素
     * 3、每次判断读指针指向的元素是否需要被删除（是否被包含在了参数集合对象）
     * 4、如果需要被删除，则写指针位置不变，读指针继续移动
     * 5、如果不需要被删除，则将读指针指向的元素复制到写指针指向位置，写指针向右移动
     * 6、遍历结束之后，写指针左侧所有的元素已经整理好了（需要被保留的）
     * 7、从写指针位置开始，到最后一个元素位置，都是需要被清理的空间
     */
    // r:read，读指针。w:write，写指针。
    int w = 0;
    for (int r = 0; r < size; r++) {
      // 判断本次读指针指向的元素在传入的集合中是否出现
      if (c.contains(elementData[r]) == rule) {
        elementData[w++] = elementData[r];
      }
    }
    // 当循环结束之后，索引小于w的所有元素就是已经整理好的要保留的元素
    // [w]~[size-1]范围都是无用元素，将其设为null值，加速gc回收
    for (int i = w; i < size; i++) {
      elementData[i] = null;
    }

    // 计算最终返回的结果
    boolean modified = w < size;

    size = w;

    return modified;
  }

  /**
   * 将当前集合中存在与集合c相同的元素删除（集合求差集）
   * @param c
   * @return
   */
  @Override
  public boolean removeAll(Collection c) {
    //集合化为数组
    Object[] newArray = c.toArray();

    //遍历删除
    for (int i = 0; i < newArray.length; i++) {
      remove(newArray[i]);
    }
    return batchRemove(c, false);
  }

  /**
   * 在当前集合保留集合c中出现过的元素（求交集）
   * @param c
   * @return
   */
  @Override
  public boolean containsAll(Collection c) {
    return batchRemove(c, true);
  }

  @Override
  public ListIterator listIterator() {
    return null;
  }

  @Override
  public ListIterator listIterator(int index) {
    return null;
  }

  @Override
  public List subList(int fromIndex, int toIndex) {
    return null;
  }

  @Override
  public Object[] toArray(Object[] a) {
    return new Object[0];
  }

  @Override
  public String toString() {
    String s = "[";
    for (int i = 0; i < size; i++) {
      s += elementData[i];
      if (i < size - 1) {
        s += ", ";
      }
    }
    s += "]";
    return s;
  }



}