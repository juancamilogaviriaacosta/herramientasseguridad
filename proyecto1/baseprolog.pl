/*
HERRAMIENTAS DE SEGURIDAD
JUAN CAMILO GAVIRIA
NICOLAS PINZON
*/

/* Definicion de permisos */
:- dynamic permiso_peligroso/1.

/* Definicion de combinaciones peligrosas */
:- dynamic combinacion_peligrosa/1.

/* Relacion de aplicacion con permisos */
:- dynamic aplicacion_permiso/2.

/* Definicion de aplicacion peligrosa cuando contiene algun permiso peligroso */
aplicacion_peligrosa_simple_tmp(X):- findall(A, (aplicacion_permiso(A,P), permiso_peligroso(P1), member(P1, P)), X).
aplicacion_peligrosa_simple(X):- aplicacion_peligrosa_simple_tmp(Y), list_to_set(Y, Z), sort(Z, X).

/* Definicion de aplicacion peligrosa cuando contiene alguna combinacion peligrosa */
aplicacion_peligrosa_combinacion_tmp(X):- findall(A, (combinacion_peligrosa(C1), sort(C1, C2), aplicacion_permiso(A, P1), sort(P1, P2), subset(C2, P2)), X).
aplicacion_peligrosa_combinacion(X):- aplicacion_peligrosa_combinacion_tmp(Y), list_to_set(Y, Z), sort(Z, X).

/* Union y sin repetidos de los dos anteriores*/
aplicaciones_peligrosas(X):- aplicacion_peligrosa_simple(A), aplicacion_peligrosa_combinacion(B), union(A,B,Y), list_to_set(Y,Z), sort(Z, X).



/* Ejemplos SI se desea probar el programa sin el parser java
permiso_peligroso(pINTERNET).
permiso_peligroso(pSOCIAL).
permiso_peligroso(pLLAMAR).

combinacion_peligrosa([pSOCIAL, pLLAMAR, pINTERNET]).
combinacion_peligrosa([pINTERNET, pLLAMAR]).
combinacion_peligrosa([pINTERNET, pSOCIAL]).

aplicacion_permiso(app1, [pINTERNET, pSOCIAL, pLLAMAR]).
aplicacion_permiso(app2, [pINTERNET]).
aplicacion_permiso(app3, [pINTERNET, pLLAMAR]).
aplicacion_permiso(app4, [pOTRO]).
*/
