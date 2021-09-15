update act_re_model m set m.category_=
(select t.type_code from wsd_wf_biztype t where t.id=
(select a.type_id from wsd_wf_assign a where a.model_id=m.id_));

update act_re_procdef d set d.category_=
(select m.category_ from act_re_model m where m.key_=d.key_);

update act_re_deployment e set e.category_=
(select d.category_ from act_re_procdef d where d.deployment_id_=e.id_);
