package com.example.pension_project.search.config;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class OracleTextIndexInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void syncIndexesOnStartup() {
        List<String> indexes = List.of(
            "idx_faq_question", "idx_faq_answer",
            "idx_notice_title", "idx_notice_content",
            "idx_manual_filename", "idx_manual_title",
            "idx_asset_title",
            "idx_principal_name", "idx_default_name", "idx_fund_name", "idx_etf_name", "idx_tdf_name"
        );

        for (String index : indexes) {
            entityManager.createNativeQuery("BEGIN CTX_DDL.SYNC_INDEX('" + index + "'); END;")
                         .executeUpdate();
        }
    }
}