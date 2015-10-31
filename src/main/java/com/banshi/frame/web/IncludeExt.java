package com.banshi.frame.web;

import org.apache.velocity.app.event.EventHandlerUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.InputBase;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.ParserTreeConstants;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.resource.Resource;

import java.io.IOException;
import java.io.Writer;

public class IncludeExt extends InputBase {
    private String outputMsgStart = "";
    private String outputMsgEnd = "";

    /**
     * Return name of this directive.
     *
     * @return The name of this directive.
     */
    public String getName() {
        return "my_include";
    }

    /**
     * Return type of this directive.
     *
     * @return The type of this directive.
     */
    public int getType() {
        return LINE;
    }

    /**
     * Since there is no processing of content,
     * there is never a need for an internal scope.
     */
    public boolean isScopeProvided() {
        return false;
    }

    /**
     * simple init - init the tree and get the elementKey from
     * the AST
     *
     * @param rs
     * @param context
     * @param node
     * @throws org.apache.velocity.exception.TemplateInitException
     */
    public void init(RuntimeServices rs, InternalContextAdapter context,
                     Node node)
            throws TemplateInitException {
        super.init(rs, context, node);

        /*
         *  get the msg, and add the space so we don't have to
         *  do it each time
         */
        outputMsgStart = rsvc.getString(RuntimeConstants.ERRORMSG_START);
        outputMsgStart = outputMsgStart + " ";

        outputMsgEnd = rsvc.getString(RuntimeConstants.ERRORMSG_END);
        outputMsgEnd = " " + outputMsgEnd;
    }

    /**
     * iterates through the argument list and renders every
     * argument that is appropriate.  Any non appropriate
     * arguments are logged, but render() continues.
     *
     * @param context
     * @param writer
     * @param node
     * @return True if the directive rendered successfully.
     * @throws java.io.IOException
     * @throws org.apache.velocity.exception.MethodInvocationException
     * @throws org.apache.velocity.exception.ResourceNotFoundException
     */
    public boolean render(InternalContextAdapter context,
                          Writer writer, Node node)
            throws IOException, MethodInvocationException,
            ResourceNotFoundException {
        /*
         *  get our arguments and check them
         */

        int argCount = node.jjtGetNumChildren();

        for (int i = 0; i < argCount; i++) {
            /*
             *  we only handle StringLiterals and References right now
             */

            Node n = node.jjtGetChild(i);

            if (n.getType() == ParserTreeConstants.JJTSTRINGLITERAL ||
                    n.getType() == ParserTreeConstants.JJTREFERENCE) {
                if (!renderOutput(n, context, writer))
                    outputErrorToStream(writer, "error with arg " + i
                            + " please see log.");
            } else {
                String msg = "invalid #include() argument '"
                        + n.toString() + "' at " + Log.formatFileString(this);
                rsvc.getLog().error(msg);
                outputErrorToStream(writer, "error with arg " + i
                        + " please see log.");
                throw new VelocityException(msg);
            }
        }

        return true;
    }

    /**
     * does the actual rendering of the included file
     *
     * @param node    AST argument of type StringLiteral or Reference
     * @param context valid context so we can render References
     * @param writer  output Writer
     * @return boolean success or failure.  failures are logged
     * @throws IOException
     * @throws MethodInvocationException
     * @throws ResourceNotFoundException
     */
    private boolean renderOutput(Node node, InternalContextAdapter context,
                                 Writer writer)
            throws IOException, MethodInvocationException,
            ResourceNotFoundException {
        if (node == null) {
            rsvc.getLog().error("#include() null argument");
            return false;
        }

        /*
         *  does it have a value?  If you have a null reference, then no.
         */
        Object value = node.value(context);
        if (value == null) {
            rsvc.getLog().error("#include() null argument");
            return false;
        }

        /*
         *  get the path
         */
        String sourcearg = value.toString();

        /*
         *  check to see if the argument will be changed by the event handler
         */

        String arg = EventHandlerUtil.includeEvent(rsvc, context, sourcearg, context.getCurrentTemplateName(), getName());

        /*
         *   a null return value from the event cartridge indicates we should not
         *   input a resource.
         */
        boolean blockinput = false;
        if (arg == null)
            blockinput = true;

        Resource resource = null;

        try {
            if (!blockinput)
                resource = rsvc.getContent(arg, getInputEncoding(context));
        } catch (ResourceNotFoundException rnfe) {
            /*
             * the arg wasn't found.  Note it and throw
             */
            rsvc.getLog().error("#include(): cannot find resource '" + arg +
                    "', called at " + Log.formatFileString(this));
            //throw rnfe;
        }

        /**
         * pass through application level runtime exceptions
         */ catch (RuntimeException e) {
            rsvc.getLog().error("#include(): arg = '" + arg +
                    "', called at " + Log.formatFileString(this));
            throw e;
        } catch (Exception e) {
            String msg = "#include(): arg = '" + arg +
                    "', called at " + Log.formatFileString(this);
            rsvc.getLog().error(msg, e);
            throw new VelocityException(msg, e);
        }


        /*
         *    note - a blocked input is still a successful operation as this is
         *    expected behavior.
         */

        if (blockinput)
            return true;

        else if (resource == null)
            return false;

        writer.write((String) resource.getData());
        return true;
    }

    /**
     * Puts a message to the render output stream if ERRORMSG_START / END
     * are valid property strings.  Mainly used for end-user template
     * debugging.
     *
     * @param writer
     * @param msg
     * @throws IOException
     */
    private void outputErrorToStream(Writer writer, String msg)
            throws IOException {
        if (outputMsgStart != null && outputMsgEnd != null) {
            writer.write(outputMsgStart);
            writer.write(msg);
            writer.write(outputMsgEnd);
        }
    }
}
