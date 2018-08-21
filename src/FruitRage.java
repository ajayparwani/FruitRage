import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class FruitRage {
static  ArrayList<Integer> value=new ArrayList<Integer>();// to get all different values in 2d array
static  ArrayList<ArrayList<temppos>> store= new ArrayList<ArrayList<temppos>>();
static  ArrayList <temppos> tempstore =new ArrayList<temppos>();
static node solutionNode;
char c='0';
static int depth=0;
static int maxdepth=5;
    static int width = 3;
    static int n = 0;
    static float t=0;


    static class temppos {
        int row;
        int col;


        temppos(int row, int col) {
            this.row = row;
            this.col = col;


        }





        public boolean equals(Object o){
            return ((temppos) o).row==row && ((temppos) o).col==col;
        }


    }
    public static class node
    {
        int rowi;
        int colj;
        int score;
        int myscore;
        int oppscore;
        char [][] upperarr;
        char [][] curarr;
        ArrayList<temppos> allstore;

        node(int rowi, int colj, char[][] tempnode, int large)
        {
            this.rowi=rowi;
            this.colj=colj;
            this.upperarr=tempnode;
            score = large;
        }

        node(node a){
            this.rowi = a.rowi;
            this.colj = a.colj;
            this.upperarr = a.upperarr;
            this.curarr = a.curarr;
        }

        int evalFunction(){
            return myscore-oppscore;
        }

        public void setAllstore(ArrayList<temppos> allstore) {
            this.allstore = new ArrayList<>(allstore);
        }

        public void setcurarr(char[][] nakliarr)
        {
            curarr=nakliarr;
        }
        public void setMyscore(int myscore)
        {
            this.myscore=myscore;
        }
        public void setOppscore(int oppscore)
        {
            this.oppscore=oppscore;
        }
    }



    public static int cells(int i,int j,char [][] copy, int n,int d){
        if(((i<0 || j<0) || (i>=n || j>=n))  ||  Character.getNumericValue(copy[i][j]) !=d || (Character.getNumericValue(copy[i][j]) == -1 ))
            return 0;
        //System.out.println("Intermediate i:" + i +" Intermediate j: " + j );

        tempstore.add(new temppos(i,j));

        copy[i][j] = (char) -1;


        return 1 +  cells(i-1,j,copy,copy.length,d)  + cells(i,j-1,copy,copy.length,d) + cells(i,j+1,copy,copy.length,d) +  cells(i+1,j,copy,copy.length,d) ;
    }




    public static ArrayList<node> cluster(int n, char[][] copy,char[][] tarr, boolean myTurn,int myScore, int opponentScore) {

        int large = 0;
        int count=0;
        ArrayList<node> nodelist=new ArrayList<>();
        for (int k = 0; k < value.size(); k++) {
            int d=0;
           d=value.get(k);
          //System.out.println("Value of d :"+ d);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (Character.getNumericValue(copy[i][j]) == d && d!=-1)

                    {    //large = Math.max(large, cells(i, j, copy, copy.length,k));
                        large = cells(i, j, copy, copy.length, d);


                        if (myTurn) {
                            nodelist.add(new node(i,j,tarr, large*large));
                            nodelist.get(nodelist.size()-1).setMyscore(myScore+large*large);
                            nodelist.get(nodelist.size()-1).setOppscore(opponentScore);


                        } else {
                            nodelist.add(new node(i,j,tarr, large*large));
                            nodelist.get(nodelist.size()-1).setOppscore(opponentScore+large*large);
                            nodelist.get(nodelist.size()-1).setMyscore(myScore);
                        }
                        nodelist.get(nodelist.size()-1).setAllstore(tempstore);
                        tempstore.clear();

//                        System.out.println("00");
//                        printArr(nodelist.get(nodelist.size()-1).upperarr);

                    }


                }
                //Now we have node list

            }


            //System.out.println(large);
            copy = copyOf(tarr);
        }


        if (myTurn) {
            nodelist.sort(new Comparator<node>() {
                @Override //dec order sorting
                public int compare(node o1, node o2) {
                    return o2.myscore-o1.myscore;
                }
            });
        } else {
            nodelist.sort(new Comparator<node>() {
                @Override //dec order sorting
                public int compare(node o1, node o2) {
                    return o2.oppscore-o1.oppscore;
                }
            });
        }

        if(nodelist.size()>width)
        {
            nodelist = new ArrayList<>(nodelist.subList(0, width));
        }
        for(node i : nodelist){
            i.setcurarr(gravity(n, i.upperarr, i.allstore));
        }


        return nodelist;
    }


    public static char[][] gravity(int n,char[][] tarr,ArrayList<temppos> coord){
        //ArrayList<temppos> coord= new ArrayList<temppos>();

        char[][] fakecopy=copyOf(tarr);
        char[][] tarr2=new char[n][n];
        //System.out.println("Arraylist.comtains " + coord.contains(new temppos(2,0)));

        for(int i=0;i<n;i++)
        {
            for (int j = 0; j < n; j++)
            {
                if(coord.contains(new temppos(i,j)))
                {

                    fakecopy[i][j]= '*';
                }
            }
        }



        for(int i=0;i<n;i++) {
            int k=n-1;
            for (int j = n - 1; j >= 0; )
            {
                if (!(fakecopy[j][i] == '*'))
                {

                    tarr2[k][i] = fakecopy[j][i];
                    j--;
                    k--;
                }
                else
                {
                    j--;
                }

            }
            for(;k>=0;k--)
            {
                tarr2[k][i]= '*';
            }
        }


        return tarr2;


    }


    public static char[][] copyOf(char[][] original) {
        char[][] copy = new char[n][n];
        for (int i = 0; i < original.length; i++)
        {
            for (int j=0; j<n; j++){
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }
    public static int maxplay(node i,int alpha,int beta,int depthofcur)
    {

        //System.out.println("in max");
        if(depthofcur>maxdepth)
        {

            return i.evalFunction();
        }
        ArrayList<node> nodelist=cluster(n,copyOf(i.curarr),i.curarr, true,i.myscore, i.oppscore);
        if(nodelist.size()==0){
            return i.evalFunction();
        }

        for(node w : nodelist){

            int local = minplay(w,alpha,beta,depthofcur+1);
            //System.out.println("2: " + depthofcur);
            if(alpha < local){
                if(depthofcur==0){
                   // System.out.println("My score:"+w.myscore);
                    //System.out.println("Opp score:"+w.oppscore);
                    solutionNode = new node(w);
                }
                alpha = local;
            }
            if(alpha>=beta)
            {
                //System.out.println("Max Pruning: ");
                return beta;
            }
        }
        return alpha;

    }
    public static int minplay(node j,int alpha,int beta,int depthofcur)
    {
        //System.out.println("in min");
        if (depthofcur > maxdepth)
        {

            return j.evalFunction();
        }
        ArrayList<node> nodelist = cluster(n, copyOf(j.curarr), j.curarr, false,j.myscore,j.oppscore);
        if(nodelist.size()==0){
            return j.evalFunction();
        }
        //printArr(j.curarr);
        for (node s : nodelist)
        {
            beta = Integer.min(beta,maxplay(s, alpha, beta, depthofcur+1));

            if (beta <= alpha)
            {
                return alpha;
            }
        }

        return beta;
    }

    public static void main(String args[] ){



        int f = 0;
        int counter = 0;

        
        // The name of the file to open.
        String fileName = "testcases.txt";

        // This will reference one line at a time
        String line = null;

        char[][] tarr = new char[n][n];
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String strN = bufferedReader.readLine();
            String strF = bufferedReader.readLine();
            String strT = bufferedReader.readLine();
            n = Integer.parseInt(strN);
            f = Integer.parseInt(strF);
            t = Float.parseFloat(strT);
            tarr = new char[n][n];


            while ((line = bufferedReader.readLine()) != null && counter < n) {
                tarr[counter] = line.toCharArray();
                //System.out.println(tarr[counter]);
                counter++;
            }

            bufferedReader.close();

        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long start=  System.currentTimeMillis();

        setwd(t,tarr);

        char [][] copy = new char[n][n];
        value.add(Character.getNumericValue(tarr[0][0]));
        //System.out.println("first val "+value.get(0));
        for(int i = 0; i < tarr.length; i++)
        {

            for(int j = 0; j < tarr[i].length; j++)
            {
                copy[i][j] = tarr[i][j];

                if(containsele(Character.getNumericValue(tarr[i][j]))) {

                    value.add(Character.getNumericValue(tarr[i][j]));
                   // System.out.println(value.get(i));
                }

            }
        }
        node initial = new node(-1,-1,tarr,0);
        initial.myscore=0;
        initial.oppscore=0;
        initial.setcurarr(tarr);
        System.out.println(maxplay(initial,Integer.MIN_VALUE,Integer.MAX_VALUE,0));

        System.out.println(solutionNode.rowi + " " + solutionNode.colj);
        printArr(solutionNode.curarr);
        System.out.println("depth: "+maxdepth);
        System.out.println("width: "+width);

        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            char colChar = ((char)(solutionNode.colj+65));
            writer.print(colChar);

            writer.println((solutionNode.rowi+1));


            for(int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    writer.print(solutionNode.curarr[i][j]);
                }
                writer.println();

            }

            writer.close();
        }catch(IOException e){
            System.out.println(
                    "Error Printing file '"
            );

        }

        long end=System.currentTimeMillis();
        long diff=end-start;
        System.out.println("Time taken:" + diff+ "ms");

    }

    private static Boolean containsele(int q) {
        for(int u=0;u<value.size();u++)
        {
            if(value.get(u)==q)
            {
                return false;
            }
        }
        return true;

    }

    private static void printArr(char[][] tarr2){
        for(int y = 0; y < tarr2.length; y++)
        {
            for(int z = 0; z < tarr2.length; z++)
            {
                if (tarr2[y][z]!='*') {
                    System.out.print((tarr2[y][z]) + " ");
                } else {
                    System.out.print(tarr2[y][z] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void setwd(float t,char[][] board)
    {
        if(t>120)
        {
            double s=starspercent(board);
            if(s>=80 && n>=18)
            {   System.out.println("T1");
                maxdepth=8;
                width=4;

            }

            if(s>30&&s<80 && n>=18)
            {
                maxdepth=7;
                width=4;
            }
            if(s<=30&&n>=18)
            {
                maxdepth=5;
                width=3;
            }
            if(s>80 && n<18)
            {   System.out.println("T1");
                maxdepth=8;
                width=5;

            }
            if(s>30 && s<=80 &&n<18)
            {

                maxdepth=7;
               width=4;
            }
            if(s<=30&&n<18)
            {
                maxdepth=5;
                width=3;
            }
        }
        if(t>60&&t<=120)
        {
            double s=starspercent(board);
            if(s>=50 && n>=18)
            {
                maxdepth=6;
                width=4;

            }
            if(s<50 && n>=18)
            {
                maxdepth=5;
                width=4;
            }
            if(s>80 && n<18)
            {
                maxdepth=7;
                width=4;

            }
            if(s>30 && s<=80 &&n<18)
            {

                maxdepth=6;
                width=4;
            }
            if(s<=30&&n<18)
            {
                maxdepth=5;
                width=3;
            }

        }
        if(t>18&&t<=60)
        {    double s=starspercent(board);
            if(s>=80 && n>=18)
            {
                maxdepth=6;
                width=4;

            }
            if(s>30&&s<80 && n>=18)
            {
                maxdepth=5;
                width=3;
            }
            if(s<=30 && n>=18)
            {
                maxdepth=4;
                width=3;
            }


            if(s>=80 && n<18)
        {
            maxdepth=6;
            width=4;

        }
            if(s>30&&s<80 && n<18)
            {
                maxdepth=5;
                width=4;
            }
            if(s<=30 && n<18)
            {
                maxdepth=5;
                width=3;
            }

        }
        if(t>0&&t<=18)

        {

            maxdepth=1;
            width=1;

        }

    }

    private static float starspercent(char[][] b1) {
        int c=0;
        for(int i=0;i<n;i++)
        { for(int p=0;p<n;p++)
        {
            if(b1[i][p]=='*'){
                c++;
            }
        }

        }
        float perct=(c*100)/(n*n);

        System.out.println("Stae per:" + perct);
        return perct;
    }

}
