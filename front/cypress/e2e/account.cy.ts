/// <reference types="cypress" />

describe('Account spec', () => {
  beforeEach(() => {
    cy.fixture('session-information/admin-session-information').then((adminSessionInformation) => {
      cy.intercept('POST', '/api/auth/login', adminSessionInformation);
    });

    cy.intercept('GET', '/api/session', []);

    cy.fixture('user/admin-user').then((adminUser) => {
      cy.intercept('GET', '/api/user/1', adminUser);
    });

    cy.visit('/login');
  });
  
  it('should display admin account info successfully', () => {
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('[data-testid="accountLink"]').click();

    cy.get('[data-testid="name"]').should('have.text', 'Name: Admin ADMIN');
    cy.get('[data-testid="email"]').should('have.text', 'Email: yoga@studio.com');
    cy.get('[data-testid="isAdmin"]').should('have.text', 'You are admin');
    cy.get('[data-testid="createdAt"]').should('have.text', 'Create at:  September 22, 2025');
    cy.get('[data-testid="updatedAt"]').should('have.text', 'Last update:  September 22, 2025');
  });
  
  it('should delete not admin account successfully', () => {
    cy.fixture('session-information/not-admin-session-information').then((notAdminSessionInformation) => {
      cy.intercept('POST', '/api/auth/login', notAdminSessionInformation);
    });
    
    cy.fixture('user/not-admin-user').then((notAdminUser) => {
      cy.intercept('GET', '/api/user/2', notAdminUser);
    });
    
    cy.intercept('DELETE', '/api/user/2', {});

    cy.get('input[formControlName=email]').type("notadmin@test.com");
    cy.get('input[formControlName=password]').type(`${"test!4321"}{enter}{enter}`);

    cy.get('[data-testid="accountLink"]').click();

    cy.get('[data-testid="deleteButton"]').should('be.visible').click();
    
    cy.get('simple-snack-bar').should('be.visible');
    cy.url().should('include', '/');
  });
  
  it('should go back', () => {
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.get('[data-testid="accountLink"]').click();
    cy.get('[data-testid="backButton"]').click();
    
    cy.url().should('include', '/sessions');
  });
});