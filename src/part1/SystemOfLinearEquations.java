package part1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SystemOfLinearEquations
{
    private String path;

    public SystemOfLinearEquations(String path)
    {
        this.path = path;
    }

    private Input read(String path)
    {
        if(path == null || path.isEmpty())
        {
            System.err.println("error: the path provided is null or empty.");
            System.exit(1);
        }

        BufferedReader reader = null;
        Input input = new Input();
        try
        {
            reader = new BufferedReader(new FileReader(path));

            String line;
            Integer rank;
            if((line = reader.readLine()) != null && !line.isEmpty())
            {
                try
                {
                    rank = Integer.parseInt(line);
                    if(rank <= 0)
                    {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e)
                {
                    System.err.println("error: the first line of the file '" + path + "' is not structured properly" +
                            ".\n the first line should represent the rank of the matrix coefficient.\n the rank " +
                            "number must be a positive number with the max value of '"+ Integer.MAX_VALUE + "'.");
                    throw new Exception();
                }
            }
            else
            {
                System.err.println("error: the first line of the file '" + path + "' is null or empty.");
                throw new Exception();
            }

            System.out.println("the rank of the matrix coefficient is '" + rank + "'.");

            input.coefficients = new Integer[rank][rank];

            for(int i = 0; i < rank; i++)
            {
                if((line = reader.readLine()) != null && !line.isEmpty())
                {
                    String[] tokens = line.trim().split("\\s+");
                    if(tokens.length == rank)
                    {
                        for(int j = 0; j < rank; j++)
                        {
                            try
                            {
                                input.coefficients[i][j] = Integer.parseInt(tokens[j]);
                            }
                            catch (NumberFormatException e)
                            {
                                System.err.println("error: the '" + (i + 2) + "'th line of the file '" + path + "' is" +
                                        " not structured properly.\n lines should represent the values of the matrix " +
                                        "coefficient.\n the values must be integer numbers with the min value of '" +
                                        Integer.MIN_VALUE + "' and the max value of '" + Integer.MAX_VALUE + "'.");
                                throw new Exception();
                            }
                        }
                    }
                    else
                    {
                        System.err.println("error: the file '" + path + "' doesn't provide a square coefficient " +
                                "matrix with the rank of '" + rank + "'.");
                        throw new Exception();
                    }
                }
                else
                {
                    System.err.println("error: the '" + (i + 2) + "'th line of the file '" + path + "' is null or " +
                            "empty.");
                    throw new Exception();
                }
            }

            System.out.println("the matrix coefficient is as below:");
            for(Integer[] rows : input.coefficients)
            {
                for(Integer value : rows)
                {
                    System.out.format("%12d", value);
                }
                System.out.println();
            }

            input.constants = new Integer[rank];
            if((line = reader.readLine()) != null && !line.isEmpty())
            {
                String[] tokens = line.trim().split("\\s+");
                if(tokens.length == rank)
                {
                    for(int i = 0; i < rank; i++)
                    {
                        try
                        {
                            input.constants[i] = Integer.parseInt(tokens[i]);
                        }
                        catch (NumberFormatException e)
                        {
                            System.err.println("error: the last needed line of the file '" + path + "' is not " +
                                    "structured properly.\n the last line should represent the values of the " +
                                    "constants vector.\n the values must be integer numbers with the min value of '"
                                    + Integer.MIN_VALUE + "' and the max value of '" + Integer.MAX_VALUE + "'.");
                            throw new Exception();
                        }
                    }
                }
                else
                {
                    System.err.println("error: the last needed line of the file '" + path + "' doesn't provide a " +
                            "constants vector of the length of '" + rank + "'.");
                    throw new Exception();
                }
            }
            else
            {
                System.err.println("error: the last needed line of the file '" + path + "' is null or empty.");
                throw new Exception();
            }

            System.out.println("the constants vector is as below:");
            for(Integer value : input.constants)
            {
                System.out.format("%12d\n", value);
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("error: the file '" + path + "' doesn't exist or is a directory.");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("error: an error occurred while reading from the file '" + path + "'.");
            System.exit(1);
        }
        catch (Exception e)
        {
            System.exit(1);
        }
        finally
        {
            try
            {
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException e)
            {
                System.err.println("error: an error occurred while closing the file '" + path + "'.");
                System.exit(1);
            }
        }

        return input;
    }

    private Integer getDeterminant(Integer[][] matrix)
    {
        Integer determinant = 0;

        if(matrix.length == 1)
        {
            return matrix[0][0];
        }
        else
        {
            for(int j = 0; j < matrix.length; j++)
            {
                Integer[][] minor = new Integer[matrix.length - 1][matrix.length - 1];

                for(int row = 1; row < matrix.length; row++)
                {
                    for(int col = 0; col < matrix.length; col++)
                    {
                        if(col < j)
                        {
                            minor[row - 1][col] = matrix[row][col];
                        }
                        else if(col > j)
                        {
                            minor[row - 1][col - 1] = matrix[row][col];
                        }
                    }
                }

                determinant += matrix[0][j] * new Double(Math.pow(-1, j)).intValue() * getDeterminant(minor);
            }
        }
        return determinant;
    }

    private void solve()
    {
        Input input = read(path);
        Integer determinant = getDeterminant(input.coefficients);
        System.out.println("the determinant of the coefficients matrix is '" + determinant + "'.");
    }

    private class Input
    {
        private Integer[][] coefficients;
        private Integer[] constants;
    }

    public static void main(String args[])
    {
        SystemOfLinearEquations system = new SystemOfLinearEquations("testcase.txt");
        system.solve();
    }
}