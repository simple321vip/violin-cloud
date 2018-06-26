18.1 File类
    ①指代一个文件。
    ②指代一个文件夹。如果是指代一个文件夹，我们可以点用list()方法，放回一个字符串的数组String[] files。
18.1.1 目录选择器
    我们有两种方法查看目录列表
    - 直接调用file.list()返回全部列表
    - 调用file.list(FileNameFilter)目录过滤器，返回符合规则的列表，例如以".java"结尾的文件。

    DirList.java是目录选择器FileNameFilter的基本使用。
    采用了java.util.Arrays.sort 和 String.CASE_INSENSITIVE_ORDER 进行了结果排序。

    FileNameFilter唯一的方法就是accept()回调函数，该函数会在调用file.list(FileNameFilter)时调用。源码如下：

        public String[] list(FilenameFilter filter) {
            String names[] = list();
            if ((names == null) || (filter == null)) {
                return names;
            }
            List<String> v = new ArrayList<>();
            for (int i = 0 ; i < names.length ; i++) {
                if (filter.accept(this, names[i])) {
                    v.add(names[i]);
                }
            }
            return v.toArray(new String[v.size()]);
        }
    由源码可知道，先是调用无参数的list方法返回了所有列表，然后for循环对每一个文件进行accept方法匹配，accept返回true的文件会加入新的列表中。

    由于FilenameFilter的实现类DirFilter只是为了匹配File的list(FilenameFilter)方法而存在的，我们可以用匿名内部类改写.
    代码参考DirList2：
        首先创建一个filter(String regex)的方法，它会返回一个指向FilenameFilter的一个引用。
        传向filter的参数必须是final类型的，整好和args符合。这在匿名内部类中是必须的，这样才能使用该类以外的对象。
        DirList2这种设计已经有所改进，让FilenameFilter和DirList2紧紧绑在一起。
        但是我们可以继续改进写法。
    代码参考DirList3：
        既然匿名内部类需要的参数是main方法传进来的，直接在list()方法内编写匿名内部类。

    思考：DirList3通过匿名内部类创建特定的一次性的类，解决问题，此方法的优点是将解决特定问题的代码隔离，聚拢于一点。
         而另一方面，不宜与阅读，谨慎使用。
------------------------------------------------------------------------------------------------------------------------
练习1：
练习2：
练习3：