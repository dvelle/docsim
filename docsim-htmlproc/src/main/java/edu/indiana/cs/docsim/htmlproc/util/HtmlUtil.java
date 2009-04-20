package edu.indiana.cs.docsim.htmlproc.util;

import java.util.logging.Logger;

public class HtmlUtil {

    private static Logger logger =
        Logger.getLogger(HtmlUtil.class.getName());

    public static String removeIFrame(String doc) throws Exception {
        // logger.info("original:\n" + doc);

        return removeTag(doc, "iframe");
        // int idx, offset, startidx=0;
        // int start, stop, ndelete = 0;
        // StringBuilder sb = new StringBuilder(doc);
        // doc = doc.toLowerCase();

        // while (true) {
        //     idx = doc.indexOf("<iframe>", startidx);
        //     if (idx != -1) {
        //         offset = "<iframe>".length();
        //     } else {
        //         idx = doc.indexOf("<iframe ", startidx);
        //         if (idx != -1 ) {
        //             offset = "<iframe ".length();
        //         } else {
        //             break;
        //         }
        //     }

        //     startidx = idx + offset;
        //     start = idx;

        //     idx = doc.indexOf("</iframe>", startidx);
        //     if (idx != -1) {
        //         offset = "</iframe>".length();
        //     } else {
        //         idx = doc.indexOf("</iframe >", startidx);
        //         if (idx != -1 ) {
        //             offset = "</iframe >".length();
        //         } else {
        //             idx = doc.indexOf("</ iframe>", startidx);
        //             if (idx != -1) {
        //                 offset = "</ iframe>".length();
        //             } else {
        //                 break;
        //             }
        //         }
        //     }

        //     startidx = idx + offset;
        //     stop = startidx;
        //     sb.delete(start-ndelete, stop-ndelete);
        //     ndelete += (stop-start);
        // }

        // doc = sb.toString();
        // ndelete = 0;
        // startidx = 0;
        // while (true) {
        //     idx = doc.indexOf("<iframe ", startidx);
        //     if (idx != -1) {
        //         offset = "<iframe ".length();
        //     } else {
        //         break;
        //     }

        //     startidx = idx + offset;
        //     start = idx;

        //     idx = doc.indexOf("/>", startidx);
        //     if (idx != -1) {
        //         offset = "/>".length();
        //     } else {
        //         break;
        //     }

        //     startidx = idx + offset;
        //     stop = startidx;
        //     sb.delete(start-ndelete, stop-ndelete);
        //     ndelete += (stop-start);
        // }
        // return sb.toString();
    }

    public static String removeMeta(String doc) throws Exception {
        // logger.info("original:\n" + doc);
        String doc2 = removeTag(doc, "meta");
        // logger.info("after:\n" + doc2);
        return doc2;
        // return removeTag(doc, "meta");
    }

    private static String removeTag (String doc, String tagName) {
        int idx, offset, startidx=0;
        int start, stop, ndelete = 0;
        StringBuilder sb = new StringBuilder(doc);
        doc = doc.toLowerCase();

        // delete <tag>content</tag>
        while (true) {
            idx = doc.indexOf("<"+tagName+">", startidx);
            if (idx != -1) {
                offset = ("<"+tagName+">").length();
            } else {
                idx = doc.indexOf("<"+tagName+" ", startidx);
                if (idx != -1 ) {
                    offset = ("<"+tagName+" ").length();
                } else {
                    break;
                }
            }

            startidx = idx + offset;
            start = idx;

            idx = doc.indexOf("</"+tagName+">", startidx);
            if (idx != -1) {
                offset = ("</"+tagName+">").length();
            } else {
                idx = doc.indexOf("</"+tagName+" >", startidx);
                if (idx != -1 ) {
                    offset = ("</"+tagName+" >").length();
                } else {
                    idx = doc.indexOf("</ "+tagName+">", startidx);
                    if (idx != -1) {
                        offset = ("</ "+tagName+">").length();
                    } else {
                        offset = -1;
                    }
                }
            }
            if (offset == -1){
                idx = doc.indexOf(">", startidx);
                if (idx != -1)
                    offset = 1;
                else
                    break;
            }

            startidx = idx + offset;
            stop = startidx;
            sb.delete(start-ndelete, stop-ndelete);
            ndelete += (stop-start);
        }

        doc = sb.toString();
        ndelete = 0;
        // delete <iframe attr="value" />
        startidx = 0;
        while (true) {
            idx = doc.indexOf("<"+tagName+" ", startidx);
            if (idx != -1) {
                offset = ("<"+tagName+" ").length();
            } else {
                break;
            }

            startidx = idx + offset;
            start = idx;

            idx = doc.indexOf("/>", startidx);
            if (idx != -1) {
                offset = "/>".length();
            } else {
                break;
            }

            startidx = idx + offset;
            stop = startidx;
            sb.delete(start-ndelete, stop-ndelete);
            ndelete += (stop-start);
        }
        return sb.toString();
    }
}

