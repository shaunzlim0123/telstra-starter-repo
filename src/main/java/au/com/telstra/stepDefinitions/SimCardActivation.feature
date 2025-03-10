Feature: SIM Card Activation
    # Scenario for a successful activation:
    Scenario: Successful SIM card activation
        Given a SIM card with ICCID "1255789453849037777" and customer email "user@example.com"
        When the activation request is submitted
        Then the SIM card activation should be successful
        And the activation record should have ICCID "1255789453849037777", customerEmail "user@example.com", and active "true"

    # Scenario for a failed activation:
    Scenario: Failed SIM card activation
        Given a SIM card with ICCID "8944500102198304826" and customer email "user2@example.com"
        When the activation request is submitted
        Then the SIM card activation should fail
        And the activation record should have ICCID "8944500102198304826", customerEmail "user2@example.com", and active "false"
