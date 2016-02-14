package part1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SystemOfLinearEquations
{
    private String path;
    private Input input;
    private Output output;
    private boolean print;

    public SystemOfLinearEquations(String path)
    {
        this.path = path;
        this.print = false;
    }

    private void print(Double[][] matrix)
    {
        for(Double[] rows : matrix)
        {
            for(Double value : rows)
            {
                System.out.format("%12.2f", value);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void print(Double[] vector)
    {
        for(Double value : vector)
        {
            System.out.format("%12.2f\n", value);
        }
        System.out.println();
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

            if(print)
            {
                System.out.println("the rank of the matrix coefficient = " + rank + "\n");
            }

            input.coefficients = new Double[rank][rank];

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
                                input.coefficients[i][j] = Double.parseDouble(tokens[j]);
                            }
                            catch (NumberFormatException e)
                            {
                                System.err.println("error: the '" + (i + 2) + "'th line of the file '" + path + "' is" +
                                        " not structured properly.\n lines should represent the values of the matrix " +
                                        "coefficient.\n the values must be float numbers with the min value of '" +
                                        Double.MIN_VALUE + "' and the max value of '" + Double.MAX_VALUE + "'.");
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

            if(print)
            {
                System.out.println("the matrix coefficient:");
                print(input.coefficients);
            }

            input.constants = new Double[rank];
            if((line = reader.readLine()) != null && !line.isEmpty())
            {
                String[] tokens = line.trim().split("\\s+");
                if(tokens.length == rank)
                {
                    for(int i = 0; i < rank; i++)
                    {
                        try
                        {
                            input.constants[i] = Double.parseDouble(tokens[i]);
                        }
                        catch (NumberFormatException e)
                        {
                            System.err.println("error: the last needed line of the file '" + path + "' is not " +
                                    "structured properly.\n the last line should represent the values of the " +
                                    "constants vector.\n the values must be float numbers with the min value of '" +
                                    Double.MIN_VALUE + "' and the max value of '" + Double.MAX_VALUE + "'.");
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

            if(print)
            {
                System.out.println("the constants vector:");
                print(input.constants);
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

    private Double getDeterminant(Double[][] matrix)
    {
        Double determinant = 0.0;

        if(matrix.length == 1)
        {
            return matrix[0][0];
        }
        else
        {
            for(int j = 0; j < matrix.length; j++)
            {
                Double[][] minor = new Double[matrix.length - 1][matrix.length - 1];

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

                determinant += matrix[0][j] * Math.pow(-1, j) * getDeterminant(minor);
            }
        }
        return determinant;
    }

    private Double getDeterminant(Integer index)
    {
        Double[][] coefficients = new Double[input.coefficients.length][input.coefficients[0].length];
        for(int i = 0; i < input.coefficients.length; i++)
        {
            for(int j = 0; j < input.coefficients[0].length; j++)
            {
                coefficients[i][j] = input.coefficients[i][j];
            }
        }

        for(int counter = 0; counter < input.constants.length; counter++)
        {
            coefficients[counter][index] = input.constants[counter];
        }

        if(print)
        {
            System.out.println("the '" + (index + 1) + "'th unknown matrix:");
            print(coefficients);
        }

        return getDeterminant(coefficients);
    }

    private void solve()
    {
        this.input = read(path);

        Double determinant = getDeterminant(input.coefficients);

        if(determinant == 0.0)
        {
            System.out.println("the system is not linear.");
            return;
        }

        this.output = new Output();
        output.unknowns = new Double[input.constants.length];
        for(int counter = 0; counter < input.constants.length; counter++)
        {
            output.unknowns[counter] = getDeterminant(counter) / determinant;
        }

        if(print)
        {
            System.out.println("the unknown values:");
            print(output.unknowns);
        }
        else
        {
            for(Double value : output.unknowns)
            {
                System.out.format("%.2f\n", value);
            }
        }
    }

    private class Input
    {
        private Double[][] coefficients;
        private Double[] constants;
    }

    private class Output
    {
        private Double[] unknowns;
    }

    public static void main(String args[])
    {
        if(args.length == 1)
        {
            SystemOfLinearEquations system = new SystemOfLinearEquations(args[0]);
            system.solve();
        }
        else
        {
            System.err.println("error: the input is invalid. it should be the path of the input file.");
        }
    }
}