package org.spoofax.lang.jasmin.strategies;

import static org.spoofax.interpreter.core.Tools.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.spoofax.interpreter.library.IOAgent;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.Strategy;

public class write_class_file_0_1 extends Strategy {

    public static write_class_file_0_1 instance = new write_class_file_0_1();

    @Override public IStrategoTerm invoke(Context context, IStrategoTerm current, IStrategoTerm fname) {
        String path = null;
        if(isTermString(fname)) {
            path = asJavaString(fname);
        } else {
            return null;
        }

        ClassGenerator cg = new ClassGenerator();
        byte[] bt;
        bt = cg.generateClass(context, current);

        if(bt == null) {
            return null;
        } else {
            try {
                final IOAgent agent = context.getIOAgent();
                final String workingDir = agent.getWorkingDir();
                final String fullPath = workingDir + "/" + path;
                final File fullDir = new File(fullPath).getParentFile();

                context.getIOAgent().printError(path);
                context.getIOAgent().printError(workingDir);
                context.getIOAgent().printError(fullPath);
                context.getIOAgent().printError("" + fullDir.mkdirs());

                OutputStream fileOutputStream = context.getIOAgent().openFileOutputStream(path);
                fileOutputStream.write(bt);
                fileOutputStream.close();
                return current;
            } catch(IOException e) {
                context.getIOAgent().printError(e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }
}
