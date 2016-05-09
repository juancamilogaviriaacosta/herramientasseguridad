/*
HERRAMIENTAS DE SEGURIDAD
JUAN CAMILO GAVIRIA
NICOLAS PINZON
*/

/* Definicion de permisos */
:- dynamic permiso_peligroso/1.

/* Definicion de combinaciones peligrosas */
:- dynamic combinacion_peligrosa/2.

/* Relacion de aplicacion con permisos */
:- dynamic aplicacion_permiso/2.

/* Definicion de aplicacion peligrosa cuando contiene algun permiso peligroso */
aplicacion_peligrosa_simple(X):- findall(A, (aplicacion_permiso(A,P), permiso_peligroso(P)), X).

/* Definicion de aplicacion peligrosa cuando contiene alguna combinacion peligrosa */
aplicacion_peligrosa_combinacion(X):- findall(A, (combinacion_peligrosa(P,Q), aplicacion_permiso(A,P), aplicacion_permiso(A,Q)), X).

/* Union y sin repetidos de los dos anteriores*/
aplicaciones_peligrosas(X):- aplicacion_peligrosa_simple(A), aplicacion_peligrosa_combinacion(B), union(A,B,Y), list_to_set(Y,X).






/* Ejemplos
permiso_peligroso(pINTERNET).
permiso_peligroso(pSOCIAL).
permiso_peligroso(pLLAMAR).

combinacion_peligrosa(pINTERNET, pLLAMAR).
combinacion_peligrosa(pINTERNET, pSOCIAL).

aplicacion_permiso(app1, pINTERNET).
aplicacion_permiso(app1, pSOCIAL).
aplicacion_permiso(app1, pLLAMAR).
aplicacion_permiso(app2, pINTERNET).
aplicacion_permiso(app3, pOTRO).
*/
