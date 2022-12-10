package org.example;

import cn.edu.whu.cstar.testingcourse.cfgparser.ControlFlowNodeParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class YuanLai {
    public static int lastNode;
    public static void main(String[] args) throws IOException {
        if(args.length != 2)
        {
            System.out.println("Error for parameter numbers. Try");
            System.out.println("\tjava -jar <name>.jar <javafile> <methodname>");
            return;
        }
        YuanLai yuanLai =new YuanLai();
        yuanLai.transaction(args[0],args[1]);
    }

    //汇总方法。
    public static void transaction(String inJavaFile,String methodNameUnion) throws IOException {
        readJavaFile(inJavaFile,methodNameUnion);
        ArrayList<ArrayList<Node>> list=getNodeListFromFile();
        list=reorder(list);
        for(int i=0;i<list.size();i++){
            ArrayList<Node> currentFunction=list.get(i);
            int[][] m=new int[maxNode(currentFunction)+1][maxNode(currentFunction)+1];
            lastNode=-1;
            buildGraph(currentFunction,0,m,0,currentFunction.size()-1,-1);
            washMatrix(m);
            System.out.println("----------------------------------------");
            System.out.println(currentFunction.get(0).getMethod()+"[Row:"+currentFunction.get(0).getRow()+"]");
            System.out.println("Control Flow Graph");
            outEdge(m,currentFunction);
            getCoverage(m,minNode(currentFunction));
            System.out.println();
        }
//        out(list);
    }

    //此方法为路径以及路径覆盖算法的汇总方法
    public static void getCoverage(int[][] m,int min){
        ArrayList<ArrayList<Integer>> primePath=primePath(m,min);
        ArrayList<ArrayList<Integer>> beginToEnd=beginToEnd(primePath,m,min);
//        outPath(beginToEnd);
        ArrayList<ArrayList<Integer>> loop=loop(primePath);
//        outPath(loop);
        System.out.println("");
        ArrayList<ArrayList<Integer>> brachCoverage=switchLoop(beginToEnd,loop);
        System.out.println("Branch Path Coverage");
        outPath(brachCoverage);
        System.out.println("");

        System.out.println("Prime Path");
        outPath(primePath);
        System.out.println();

        ArrayList<ArrayList<Integer>> primeCoverage=addLoop(beginToEnd,loop);
        System.out.println("Prime Path Coverage");
        outPath(primeCoverage);
//        ArrayList<ArrayList<Integer>> primeCoverage=
//        outPath(res);
    }

    //此方法为分支路径覆盖的生成算法，将环替代beginToEnd路径中的简单节点，即可覆盖全部路径。
    public static ArrayList<ArrayList<Integer>> switchLoop(ArrayList<ArrayList<Integer>> beginToEnd,ArrayList<ArrayList<Integer>> loop){
        ArrayList<ArrayList<Integer>> res=(ArrayList<ArrayList<Integer>>) beginToEnd.clone();
        ArrayList<ArrayList<Integer>> res1=(ArrayList<ArrayList<Integer>>) res.clone();
        ArrayList<ArrayList<Integer>> clone=(ArrayList<ArrayList<Integer>>) loop.clone();
        ArrayList<ArrayList<Integer>> clone2=(ArrayList<ArrayList<Integer>>) clone.clone();
        while(clone.size()>0) {
            for (ArrayList<Integer> currentLoop : clone2) {
                boolean flag = false;
                for (ArrayList<Integer> list : res1) {
                    for (int i : list) {
                        if (currentLoop.contains(i)) {
                            ArrayList<Integer> temp = replaceLoop(list, currentLoop, 1);
                            res.set(res1.indexOf(list), temp);
                            clone.remove(clone2.indexOf(currentLoop));
                            flag = true;
                            break;
                        }
                    }
                    if (flag) break;
                }
                if(flag) break;
            }
            clone2=(ArrayList<ArrayList<Integer>>) clone.clone();
            res1=(ArrayList<ArrayList<Integer>>) res.clone();
        }
        return res;
    }

    //此方法为主路径覆盖的路径生成算法，将环合理的插入到beginToEnd的路径中，获得一条新的覆盖主路径的路径
    public static ArrayList<ArrayList<Integer>> addLoop(ArrayList<ArrayList<Integer>> beginToEnd,ArrayList<ArrayList<Integer>> loop){
        ArrayList<ArrayList<Integer>> res=(ArrayList<ArrayList<Integer>>) beginToEnd.clone();
        ArrayList<ArrayList<Integer>> res1=(ArrayList<ArrayList<Integer>>) res.clone();
        ArrayList<ArrayList<Integer>> clone=(ArrayList<ArrayList<Integer>>) loop.clone();
        ArrayList<ArrayList<Integer>> clone2=(ArrayList<ArrayList<Integer>>) clone.clone();
        while(clone.size()>0) {
            for (ArrayList<Integer> currentLoop : clone2) {
                boolean flag = false;
                for (ArrayList<Integer> list : res1) {
                    for (int i : list) {
                        if (currentLoop.contains(i)) {
                            ArrayList<Integer> temp = replaceLoop(list, currentLoop, 2);
                            res.add(temp);
                            clone.remove(clone2.indexOf(currentLoop));
                            flag = true;
                            break;
                        }
                    }
                    if (flag) break;
                }
                if(flag) break;
            }
            clone2=(ArrayList<ArrayList<Integer>>) clone.clone();
            res1=(ArrayList<ArrayList<Integer>>) res.clone();
        }
        return res;
    }

    //用环替换路径中对应的简单节点
    public static ArrayList<Integer> replaceLoop(ArrayList<Integer> orginPath,ArrayList<Integer> loop,int time){
        int start=-1;
        int end=-1;
        for(int i=orginPath.size()-1;i>=0;i--){// 此处重点在于，从后往前遍历路径，因为要把环添加到路径尾部
            if(loop.contains(orginPath.get(i))) {
                end=i+1;
                break;
            }
        }
        for(int i=end-1;i>=0;i--){
            if(!loop.contains(orginPath.get(i))){
                start=i+1;
                break;
            }
        }
        ArrayList<Integer> insertLoop=getAddLoop(loop,orginPath.get(start));
        ArrayList<Integer> res=new ArrayList<>();
        for(int i=0;i<start;i++){
            res.add(orginPath.get(i));
        }
        for(int i=0;i<time;i++){
            for(int j=0;j<insertLoop.size();j++){
                res.add(insertLoop.get(j));
            }
        }
        res.add(orginPath.get(start));
        for(int i=end;i<orginPath.size();i++){
            res.add(orginPath.get(i));
        }
        return res;
    }

    // 要将环正确添加到路径中，此方法帮助获得插入的环状路径的节点list
    public static ArrayList<Integer> getAddLoop(ArrayList<Integer> list,int element){// 要将环正确添加到路径中，此方法帮助获得插入的环状路径的节点list
        int index=list.indexOf(element);
        ArrayList<Integer> temp=new ArrayList<>();
        for(int i=index;i<list.size();i++){
            temp.add(list.get(i));
        }
        for(int i=0;i<index;i++){
            temp.add(list.get(i));
        }
        ArrayList<Integer> res=new ArrayList<>();
        for(int i=0;i<temp.size();i++){
            res.add(temp.get(i));
        }
        return res;
    }

    //获取主路径中的环状路径，相同的环需要剔除
    public static ArrayList<ArrayList<Integer>> loop(ArrayList<ArrayList<Integer>> list1){//获取主路径中的环状路径，相同的环需要剔除
        ArrayList<ArrayList<Integer>> list=(ArrayList<ArrayList<Integer>>) list1.clone();
        ArrayList<ArrayList<Integer>> res=new ArrayList<>();
        for(ArrayList<Integer> temp:list){
            if(temp.get(0)==temp.get(temp.size()-1) && temp.size()>=2){
                boolean flag=true;
                for(ArrayList<Integer> temp2:res) {
                    if (sameLoop(temp, temp2)) {
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    ArrayList<Integer> temp2=(ArrayList<Integer>) temp.clone();
                    temp2.remove(temp2.size()-1);
                    res.add(temp2);
                }
            }
        }
        return res;
    }

    //判断两个环状路径是否是同一个环
    public static boolean sameLoop(ArrayList<Integer> loop1,ArrayList<Integer> loop2){ //判断两个环状路径是否是同一个环
        for(int i:loop1){
            if(!loop2.contains(i)) return false;
        }
        return true;
    }

    //获取主路径中从初始节点到终止节点的路径
    public static ArrayList<ArrayList<Integer>> beginToEnd(ArrayList<ArrayList<Integer>> list,int[][] m,int min){//获取从初始节点到终止节点的路径
        ArrayList<Integer> begin=getBegin(m,min);
        ArrayList<Integer> end=getEnd(m,min);
        ArrayList<ArrayList<Integer>> beginToEnd=new ArrayList<>();
        for(ArrayList<Integer> temp:list){
            if(begin.contains(temp.get(0)) && end.contains(temp.get(temp.size()-1))){
                beginToEnd.add(temp);
            }
        }
        return beginToEnd;
    }

    //主路径的获取算法，通过simplepath的进一步筛选得到
    public static ArrayList<ArrayList<Integer>> primePath(int[][] m,int min){
        ArrayList<ArrayList<Integer>> primePath=new ArrayList<>();
        ArrayList<Path> simplePathList=simplePath(m,min);
        for(Path path:simplePathList){
            ArrayList<Integer> temp=path.getPath();
            ArrayList<Integer> sourceList=sourceList(m,temp.get(0));
            if(temp.get(0)==temp.get(temp.size()-1) && temp.size()>=2) {//如果是环状路径，直接加入主路径中
                primePath.add(temp);
                continue;
            }
            boolean flag=false;
            for(int a:sourceList){
                if(!temp.contains(a)) flag=true;// 判断此路径不为其它简单路径的子路径
            }
            if(flag) continue;
            primePath.add(temp);
        }
        return primePath;
    }

    //通过邻接矩阵的入度获取图的初始节点
    public static ArrayList<Integer> getBegin(int[][] m,int min){
        ArrayList<Integer> begin=new ArrayList<>();
        for(int i=min;i<m.length;i++){
            boolean flag=true;
            for(int j=min;j<m.length;j++){
                if(m[j][i]==1) {
                    flag=false;
                    break;
                }
            }
            if(flag) begin.add(i);
        }
        return begin;
    }

    //通过邻接矩阵的出度获取图的终止节点
    public static ArrayList<Integer> getEnd(int[][] m,int min){
        ArrayList<Integer> begin=new ArrayList<>();
        for(int i=min;i<m.length;i++){
            boolean flag=true;
            for(int j=min;j<m.length;j++){
                if(m[i][j]==1) {
                    flag=false;
                    break;
                }
            }
            if(flag) begin.add(i);
        }
        return begin;
    }

    //获取节点的来源节点的list
    public static ArrayList<Integer> sourceList(int[][]m ,int target){
        ArrayList<Integer> list=new ArrayList<>();
        for(int i=0;i<m.length;i++){
            if(m[i][target]==1) list.add(i);
        }
        return list;
    }

    //获取图的简单路径，通过循环得到，算法参考了pdf45页开始的算法（生成的不是完全的simple path，但是完全覆盖了prime path）作为prim path的候选path
    public static ArrayList<Path> simplePath(int[][] m,int min){
        int maxLength=m.length-min+1;
        ArrayList<Path> simplePathList=new ArrayList<>();
        for(int i=min;i<m.length;i++){
            ArrayList<Integer> temp=new ArrayList<>();
            temp.add(i);
            simplePathList.add(new Path(temp,isEnd(temp,m)));
        }
        for(int i=1;i<maxLength;i++){
            ArrayList<Path> tempPathList=new ArrayList<>();
            for(Path path:simplePathList){
                if(path.state==true) {
                    tempPathList.add(path);
                    continue;
                }
                ArrayList<Integer> temp=path.getPath();
                ArrayList<Integer> candidate=getCandidate(m,temp.get(temp.size()-1));
                for(int j=0;j<candidate.size();j++){
                    if(temp.lastIndexOf(candidate.get(j))==0
                    || temp.lastIndexOf(candidate.get(j))==-1) {
                        ArrayList<Integer> clone=(ArrayList<Integer>) temp.clone();
                        clone.add(candidate.get(j));
                        tempPathList.add(new Path(clone, isEnd(clone, m)));
                    }
                }
            }
            simplePathList=tempPathList;
        }
        return simplePathList;
    }

    //获取节点的下一个节点的list
    public static ArrayList<Integer> getCandidate(int[][] m,int target){
        ArrayList<Integer> list=new ArrayList<>();
        for(int i=0;i<m.length;i++){
            if(m[target][i]==1){
                list.add(i);
            }
        }
        return list;
    }

    //判断当前路径是否走到尽头，用于simple path生成的判定。算法参考pdf 45页
    public static boolean isEnd(ArrayList<Integer> list,int[][] m){
        int target=list.get(list.size()-1);
        if(findPointer(m,target)==-1 || (list.size()>1 && list.get(0)==target)){
            return true;
        }
        return false;
    }

    //打印list中的所有路径
    public static void outPath(ArrayList<ArrayList<Integer>> list){
        for(ArrayList<Integer> temp:list){
            for(int i:temp){
                System.out.print(i+" ");
            }
            System.out.println();
        }
    }

    //建立控制流图的中枢方法，根据Node的不同content进行分支。
    public static void buildGraph(ArrayList<Node> list, int index, int[][] m, int height,int end, int after){
        concatenateSameLayer(list,height,m,index,after,end);
        int i=index;
        while(i<end){
            Node node=list.get(i);
            if(node.height==height) {
                switch (node.getContent()) {
                    case "if-statement":
                        ifStatement(list, i, m);
                        break;
                    case "for-statement":
                        forStatemente(list, i, m);
                        break;
                    case "while-statement":
                        whileStatement(list, i, m);
                        break;
                    case "return": return;
                    case "pseudo-return": return;
                }
            }
            i++;
        }
    }

    //对ifStatement的处理，主要关于if-then和if-then-else两种情况的控制流图的边的处理
    public static void ifStatement(ArrayList<Node> list,int index,int[][] m){
        int i=index;
        int temp=findPointer(m,list.get(index).getNode());
        m[list.get(index).getNode()][list.get(index+1).getNode()]=1;
        boolean flag=false;
        while(list.get(i).startx<=list.get(index).endx){
            if(list.get(i).getHeight()==list.get(index).getHeight()+1
                && list.get(i).getContent().equals("else-body")){
                flag=true;
                m[list.get(index).getNode()][temp]=0;
                m[list.get(index).getNode()][list.get(i).getNode()]=1;
                break;
            }
            i++;
        }
        if(flag){
            buildGraph(list,index+1,m,list.get(index).getHeight()+1,i-1,temp);
            buildGraph(list,i,m,list.get(index).getHeight()+1,findEndIndex(list,index),temp);
        }
        else{
            buildGraph(list,index+1,m,list.get(index).getHeight()+1,findEndIndex(list,index),temp);
        }

    }

    //对forStatement的处理，包括对for内部几个条件的控制流图的边的连接和消除
    public static void forStatemente(ArrayList<Node> list,int index,int[][] m){
        Node forStatementNode=list.get(index);
        Node forConditonNode=list.get(index+1);
        Node forUpdateNode=list.get(index+2);
        Node forBodyNode=list.get(index+3);
        int temp=findPointer(m,forStatementNode.getNode());
        m[forStatementNode.getNode()][temp]=0;
        m[forConditonNode.getNode()][temp]=1;
        m[forStatementNode.getNode()][forConditonNode.getNode()]=1;
        m[forConditonNode.getNode()][forBodyNode.getNode()]=1;
        m[forBodyNode.getNode()][forUpdateNode.getNode()]=1;
        m[forUpdateNode.getNode()][forConditonNode.getNode()]=1;
        buildGraph(list,index+3,m,forBodyNode.getHeight(),findEndIndex(list,index),forUpdateNode.getNode());
    }

    //对whileStatement的处理，包括对while内部几个条件的控制流图的边的连接和消除
    public static void whileStatement(ArrayList<Node> list,int index,int[][] m){
        Node whileStatement=list.get(index);
        Node whileBody=list.get(index+1);
        m[whileStatement.getNode()][whileBody.getNode()]=1;
        buildGraph(list,index+1,m,whileBody.getHeight(),findEndIndex(list,index),whileStatement.getNode());
    }

    //获取节点指向节点的Node编号。
    public static int findPointer(int[][] m,int node){ //找到after的node值
        int temp=-1;
        for(int a=0;a<m.length;a++){
            if(m[node][a]!=0){
                temp=a;
            }
        }
        return temp;
    }

    //获取当前方法的最大Node编号，方便建立邻接矩阵
    public static int maxNode(ArrayList<Node> list){
        int max=Integer.MIN_VALUE;
        for(Node node:list){
            max=node.getNode()>max? node.getNode():max;
        }
        return max;
    }

    //获取当前方法的最小Node编号，方便建立邻接矩阵（因为如果有重载的方法，最小Node编号不会为0）
    public static int minNode(ArrayList<Node> list){
        int min=Integer.MAX_VALUE;
        for(Node node:list){
            min=node.getNode()<min? node.getNode():min;
        }
        return min;
    }

    //将同一height的节点进行连接
    public static void concatenateSameLayer(ArrayList<Node> list,int height,int[][] m,int start,int after,int end){
        int lastNode=-1;
        int i=start;
        while(i<=end){
            Node node=list.get(i);
            if(node.getHeight()==height){
                if(lastNode!=-1 && lastNode!=node.getNode()) {
                    m[lastNode][node.getNode()] = 1;
                }
                if(node.getContent().equals("return") || node.getContent().equals("pseudo-return")){
                    return;
                }
                lastNode=node.getNode();
            }
            i++;
        }
        if(after>0){
            m[lastNode][after]=1;
        }
    }

    //去除对角线这种无效边
    public static void washMatrix(int[][] m){
        for(int i=0;i<m.length;i++){
            for(int j=0;j<m[0].length;j++){
                if(i==j) m[i][j]=0;
            }
        }
    }

    //打印邻接矩阵（方便测试）
    public static void outMatrix(int[][] m){
        for(int i=0;i<m.length;i++){
            for (int j=0;j<m[0].length;j++){
                System.out.print(m[i][j]+" ");
            }
            System.out.println("");
        }
    }

    //打印控制流图的边（此方法会输出节点信息，方便测试）
    public static void outEdgeDetail(int[][] m,ArrayList<Node> list){
        for(int i=0;i<m.length;i++){
            for(int j=0;j<m[0].length;j++){
                if(m[i][j]==1){
                    Node node1=findNode(i,list);
                    Node node2=findNode(j,list);
                    System.out.println(node1.getNode()+" "+node1.getContent()+"  "+node2.getNode()+" "+node2.getContent());
                }
            }
        }
    }

    //打印控制流图的边（此方法不会输出节点信息）
    public static void outEdge(int[][] m,ArrayList<Node> list){
        for(int i=0;i<m.length;i++){
            for(int j=0;j<m[0].length;j++){
                if(m[i][j]==1){
                    Node node1=findNode(i,list);
                    Node node2=findNode(j,list);
                    System.out.println(node1.getNode()+"  "+node2.getNode());
                }
            }
        }
    }

    //通过Node的编码从list中找到对应Node
    public static Node findNode(int nodeIndex,ArrayList<Node> list){
        for(Node node:list){
            if(node.getNode()==nodeIndex){
                return node;
            }
        }
        return null;
    }

    //获取当前block的最后一个index，通过行数判定，行数超出当前例如if的endx则开始超出。
    public static int findEndIndex(ArrayList<Node> list, int index){
        for(int i=index;i<list.size();i++){
            if(list.get(i).getStartx()>list.get(index).getEndx()){
                return i-1;
            }
        }
        return list.size()-1;
    }

    //输出获取的节点列表的信息，方便测试
    public static void out(ArrayList<ArrayList<Node>> list){
        for(int i=0;i<list.size();i++){
            for(int j=0;j<list.get(i).size();j++){
                System.out.println(list.get(i).get(j).toString());
            }
        }
    }

    // 将获得的节点列表按照行序进行重排列，方便后续建图。
    public static ArrayList<ArrayList<Node>> reorder(ArrayList<ArrayList<Node>> list){
        for(int i=0;i<list.size();i++){
            ArrayList<Node> currentList=list.get(i);
            currentList.sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.getStartx()-o2.getStartx();
                }
            });
        }
        return list;
    }

    //从本地文件中将Node写到List中
    public static ArrayList<ArrayList<Node>> getNodeListFromFile() throws IOException {
        ArrayList<ArrayList<Node>> list=new ArrayList<>();
        FileInputStream inputStream = new FileInputStream("JavaTest.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        bufferedReader.readLine();
        String str = null;
        String currentFunction = null;
        ArrayList<Node> currentList=new ArrayList<>();
        while((str = bufferedReader.readLine()) != null && str.length()!=0)
        {
            str=str.replace("["," ").replace("]"," ");
            String[] s=str.split("@")[0].split("\\s+");
            if(currentFunction!=null){
                if(!currentFunction.equals(s[1])) {
                    list.add(currentList);
                    currentList = new ArrayList<>();
                    currentFunction = s[1];
                }
            }
            else currentFunction=s[1];
            Node node=new Node(s[0],
                    Integer.parseInt(s[1]),Integer.parseInt(s[2]),
                    Integer.parseInt(s[3]),Integer.parseInt(s[4]),
                    Integer.parseInt(s[5]),Integer.parseInt(s[6]),
                    Integer.parseInt(s[7]),Integer.parseInt(s[8]),
                    s[9]);
            currentList.add(node);
        }
        list.add(currentList);
        //close
        inputStream.close();
        bufferedReader.close();

        return list;
    }

    //将老师所给方法生成的方法信息写入JavaTest.txt文件中，方便后续处理
    public static void readJavaFile(String inJavaFile, String methodNameUnion){
        String fileName="JavaTest.txt";
        try
        {
            PrintStream out = new PrintStream(fileName);
            System.setOut(out);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        ControlFlowNodeParser parser = new ControlFlowNodeParser();
        parser.parseControlFlowNodes(inJavaFile, methodNameUnion);
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}