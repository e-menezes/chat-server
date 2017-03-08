=============
CHANGELOG v3
=============
- Corrigido o programa para aderir ao contrato durante a resposta à um "cmd":"receber", ao invés de retornar as mensagens no formato {“origem”:”maria”,”msg”:”kd vc?”} o servidor retorna {“src”:”maria”,”data”:”kd vc?”}.

=============
CHANGELOG v2
=============

- Corrigido o atributo de retorno do server de "ack" para "msgNr"
- Servidor não retorna "Toc Toc" como resposta imediata ao envio de "piada", é necessário enviar um comando de "receber" para buscar a mensagem
- Já é possível testar o envio de mensagem cliente-cliente (AV2 e Prova Final)