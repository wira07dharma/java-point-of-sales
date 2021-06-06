package com.dimata.util.blob;


import java.awt.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

//import Acme.JPM.Encoders.GifEncoder;

public class ImageServlet extends HttpServlet {
    
    private Component dummy;

    public void init(ServletConfig conf)
                    throws ServletException {

        super.init(conf);
        dummy = new Frame();
        dummy.addNotify();
    }


    protected Image createImage(int w,
                    int h, Color bg) {

        Image img = dummy.createImage(w,h);
        Graphics g = img.getGraphics();
        g.setColor(bg);
        g.fillRect(0, 0, w, h);
        return img;
    }


    protected void sendImage(HttpServletResponse res, Image img)
        throws IOException
    {
        res.setContentType("image/gif");
        OutputStream out = res.getOutputStream();
        new GifEncoder(img, out, true).encode();
        out.close();
    }


    protected FontMetrics getFontMetrics(Font f) {
        return dummy.getFontMetrics(f);
    }


    protected Font getFont() {
        return dummy.getFont();
    }
}
