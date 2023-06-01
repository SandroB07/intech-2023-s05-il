package com.intech.comptabilite.service.entityservice;

import com.intech.comptabilite.model.JournalComptable;
import com.intech.comptabilite.repositories.JournalComptableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JournalComptableService {


	private final JournalComptableRepository repository;

    public JournalComptableService(JournalComptableRepository repository)
    {
        this.repository = repository;
    }

    /**
     * Renvoie le {@link JournalComptable} de code {@code pCode} s'il est présent dans la liste
     *
     * @param pList la liste où chercher le {@link JournalComptable}
     * @param pCode le code du {@link JournalComptable} à chercher
     * @return {@link JournalComptable} ou {@code null}
     */
    public JournalComptable getByCode(List<? extends JournalComptable> pList, String pCode) {
        JournalComptable vRetour = null;
        for (JournalComptable vBean : pList) {
            if (vBean != null && Objects.equals(vBean.getCode(), pCode)) {
                vRetour = vBean;
                break;
            }
        }
        return vRetour;
    }

	public List<JournalComptable> getListJournalComptable() {
		List<JournalComptable> result = new ArrayList<JournalComptable>();
		repository.findAll().forEach(result::add);
		return result;
	}
	
}
