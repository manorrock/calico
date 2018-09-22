/*
 * Copyright (c) 2002-2018 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.calico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.security.enterprise.identitystore.PasswordHash;

/**
 * The startup bean.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Singleton
@Startup
public class StartupBean {
    
    /**
     * Stores the entity manager factory.
     */
    @PersistenceContext(unitName = "calico")
    private EntityManager em;
    
    /**
     * Stores the password hash.
     */
    @Inject
    private PasswordHash passwordHash;
    
    /**
     * Get the entity manager.
     * 
     * @return the entity manager.
     */
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Initialize.
     */
    @PostConstruct
    public void initialize() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);
        if (em.createQuery("SELECT object(o) FROM UserAccount AS o").getResultList().isEmpty()) {
            UserAccount user = new UserAccount();
            user.setUsername("admin");
            user.setPassword(passwordHash.generate("calico".toCharArray()));
            em.persist(user);
            UserGroup group = new UserGroup();
            group.setGroupName("user");
            group.setUsername("admin");
            em.persist(group);
        }
        
        List<UserGroup> groups = em.createQuery("SELECT object(o) FROM UserGroup AS o").getResultList();
        if (!groups.isEmpty()) {
            groups.forEach((group) -> {
                Query findQuery = em.createQuery(
                        "SELECT object(o) FROM UserRole AS o WHERE o.roleName = :roleName AND o.username = :username");
                findQuery.setParameter("roleName", group.getGroupName());
                findQuery.setParameter("username", group.getUsername());
                if (findQuery.getResultList().isEmpty()) {
                    UserRole userRole = new UserRole();
                    userRole.setRoleName(group.getGroupName());
                    userRole.setUsername(group.getUsername());
                    em.persist(userRole);
                }
            });
        }
    }
}
