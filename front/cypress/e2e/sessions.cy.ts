/// <reference types="cypress" />

describe('Sessions spec', () => {
  beforeEach(() => {
    cy.fixture('session-information/admin-session-information').then((adminSessionInformation) => {
      cy.intercept('POST', '/api/auth/login', adminSessionInformation);
    });

    cy.fixture('session/sessions').then((sessions) => {
      cy.intercept('GET', '/api/session', sessions);
    });

    cy.fixture('session/session-1').then((session) => {
      cy.intercept('GET', '/api/session/1', session);
    });

    cy.fixture('session/session-2').then((session) => {
      cy.intercept('GET', '/api/session/2', session);
    });

    cy.fixture('teacher/teachers').then((teachers) => {
      cy.intercept('GET', '/api/teacher', teachers);
    });

    cy.fixture('teacher/teacher').then((teacher) => {
      cy.intercept('GET', '/api/teacher/1', teacher);
    });

    cy.visit('/login');
  });
  
  it('should create session', () => {
    cy.fixture('session/session-3').then((session) => {
      cy.intercept('POST', '/api/session', session);
    });

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('button[data-testid="create"]').should('be.visible').click();
    
    cy.url().should('include', '/sessions/create');
    cy.get('h1[data-testid="create"]').should('be.visible');
    
    cy.get('input[data-testid="name"]').type('Session 3');
    cy.get('input[data-testid="date"]').type('2025-10-30');
    cy.get('mat-select[data-testid="teacher"]').click();
    cy.contains('mat-option', 'Herbert Garrison').click();
    cy.get('textarea[data-testid="description"]').type('Encore une autre session');
    cy.get('button[data-testid="save"]').click();
    
    cy.get('simple-snack-bar').should('be.visible').should('contain.text', 'Session created !');
    cy.url().should('include', '/sessions');
  });
  
  it('should update session', () => {
    cy.fixture('session/session-1').then((session) => {
      cy.intercept('PUT', '/api/session/1', session);
    });

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('button[data-testid="update"]').first().click();
    
    cy.url().should('include', '/sessions/update/1');
    cy.get('h1[data-testid="update"]').should('be.visible');
    
    cy.get('input[data-testid="date"]').type('2025-10-24');
    cy.get('textarea[data-testid="description"]').type('Une session mise à jour');
    cy.get('button[data-testid="save"]').click();
    cy.get('simple-snack-bar').should('be.visible').should('contain.text', 'Session updated !');
    cy.url().should('include', '/sessions');
  });

  it('should display session detail', () => {    
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
    
    cy.get('button[data-testid="detail"]').first().click();
    
    cy.get('h1[data-testid="name"]').should('have.text', 'Session 1');
    cy.get('span[data-testid="teacher"]').should('have.text', 'Margaret TEACHER');
    cy.get('span[data-testid="attendees"]').should('have.text', '3 attendees');
    cy.get('span[data-testid="date"]').should('have.text', 'October 23, 2025');
    cy.get('div[data-testid="description"]').should('contain.text', 'Une session');
    cy.get('div[data-testid="createdAt"]').should('contain.text', 'September 22, 2025');
    cy.get('div[data-testid="updatedAt"]').should('contain.text', 'September 22, 2025');
  });

  it('should allow not admin user to participate in a session', () => {
    cy.fixture('session-information/not-admin-session-information').then((notAdminSessionInformation) => {
      cy.intercept('POST', '/api/auth/login', notAdminSessionInformation);
    });
    cy.intercept('POST', '/api/session/1/participate/2', {});
    
    cy.get('input[formControlName=email]').type("notadmin@test.com");
    cy.get('input[formControlName=password]').type(`${"test!4321"}{enter}{enter}`);

    cy.get('button[data-testid="detail"]').first().click();

    cy.get('button[data-testid="participate"]').should('be.visible').click();
  });

  it('should allow not admin user to unparticipate from a session', () => {
    cy.fixture('session-information/not-admin-session-information').then((notAdminSessionInformation) => {
      cy.intercept('POST', '/api/auth/login', notAdminSessionInformation);
    });
    cy.intercept('DELETE', '/api/session/2/participate/2', {});
    
    cy.get('input[formControlName=email]').type("notadmin@test.com");
    cy.get('input[formControlName=password]').type(`${"test!4321"}{enter}{enter}`);

    cy.get('button[data-testid="detail"]').eq(1).click();
    
    cy.get('button[data-testid="unparticipate"]').should('be.visible').click();
  });
  
  it('should go back', () => {    
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('button[data-testid="detail"]').first().click();
    cy.get('button[data-testid="back"]').click();
    
    cy.url().should('include', '/sessions');
  });

  it('should delete session', () => {
    cy.intercept('DELETE', '/api/session/1', {});    
    
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('button[data-testid="detail"]').first().click();
    
    cy.get('button[data-testid="delete"]').should('be.visible').click();
    
    cy.get('simple-snack-bar').should('be.visible').should('contain.text', 'Session deleted !');
    cy.url().should('include', '/sessions');
  });
});