#****************************************************************************
#
# Makefile for LSA (Latent Semantic Analysis) 
# Jiayi Zhu
#
# This is a GNU make (gmake) makefile
#****************************************************************************



CC     := g++

CLAPACKPATH = ../../../wmProj/CLAPACK
#INCDIRS = -I$(CLAPACKPATH)/SRC -I$(CLAPACKPATH)
F2CDIR  = $(CLAPACKPATH)/F2CLIBS
LDLIBS  = $(CLAPACKPATH)/lapack_LINUX.a \
          $(CLAPACKPATH)/blas_LINUX.a \
          $(F2CDIR)/libf2c.a -lm
          
LIBDIR = /u/jiayzhu/usr/local/lib/
          
lsa: lsa.o
	$(CC) -o lsa lsa.o $(LDLIBS)
	$(CC) lsaquery.cpp lsaquery.h -O3 -shared -o liblsaquery.so

clean:
	-rm *.o lsa *.so