-- Seed de 10 ordens para cada fila (printing, stock_withdrawal, wire_cutting),
-- variando o status entre pending, in_progress e finished.

-- ===================== PRINTING DETAILS (10 itens) =====================
INSERT INTO printing_details (
    id, work_order_number, operator_number, print_text, quantity, is_urgent, reason, status, created_at, updated_at
) VALUES
    ('5bfbca48-ce87-45e7-b49a-346db651873b', 'WO-PR-001', '1003', 'LABEL-BATCH-01', 50, FALSE, 'Label reprint for batch 1', 'pending', NOW(), NOW()),
    ('d393f73e-bb3a-4094-ab88-0cf6a86e2a67', 'WO-PR-002', '1004', 'LABEL-BATCH-02', 30, TRUE, 'Urgent reprint for batch 2', 'in_progress', NOW(), NOW()),
    ('3918be17-07a2-4e10-97bc-de6a2e7217f6', 'WO-PR-003', '1003', 'LABEL-BATCH-03', 20, FALSE, 'Standard batch 3 labeling', 'finished', NOW(), NOW()),
    ('a5ad7564-4538-4208-8ca1-cdd43f7e041d', 'WO-PR-004', '1006', 'LABEL-BATCH-04', 75, FALSE, 'Standard batch 4 labeling', 'pending', NOW(), NOW()),
    ('ce7f1e1d-8a2a-4a3e-8dee-5902d8f8e4b8', 'WO-PR-005', '1004', 'LABEL-BATCH-05', 10, TRUE, 'Urgent reprint for batch 5', 'in_progress', NOW(), NOW()),
    ('87cb280a-ef65-4a4a-a653-c79e04a8a97a', 'WO-PR-006', '1003', 'LABEL-BATCH-06', 40, FALSE, 'Standard batch 6 labeling', 'finished', NOW(), NOW()),
    ('15402f80-5fad-4691-9e63-6ec63a299453', 'WO-PR-007', '1006', 'LABEL-BATCH-07', 60, FALSE, 'Standard batch 7 labeling', 'pending', NOW(), NOW()),
    ('498194f3-398d-4c2e-8bfe-99a26ba9c735', 'WO-PR-008', '1004', 'LABEL-BATCH-08', 25, TRUE, 'Urgent reprint for batch 8', 'in_progress', NOW(), NOW()),
    ('93d72c7e-7fc5-4b65-9195-3e83cdb073f2', 'WO-PR-009', '1003', 'LABEL-BATCH-09', 15, FALSE, 'Standard batch 9 labeling', 'finished', NOW(), NOW()),
    ('e4f8db9f-62b8-4fd7-9f84-44d16a3ae9d6', 'WO-PR-010', '1006', 'LABEL-BATCH-10', 35, FALSE, 'Standard batch 10 labeling', 'pending', NOW(), NOW());

-- ===================== STOCK WITHDRAWAL DETAILS (10 itens) =====================
INSERT INTO stock_withdrawal_details (
    id, work_order_number, operator_number, item_name, quantity, is_urgent, reason, status, created_at, updated_at
) VALUES
    ('98c5a87c-5ae3-43fe-b7e3-bba9a799eb13', 'WO-ST-001', '1003', 'M6 Screw', 200, TRUE, 'Line 3 replenishment', 'pending', NOW(), NOW()),
    ('d2cd2036-2b4f-4c42-b0ed-c58482c1f4c6', 'WO-ST-002', '1004', 'M8 Nut', 150, FALSE, 'Line 1 replenishment', 'in_progress', NOW(), NOW()),
    ('ca8bffdb-56b0-4a1d-b0bc-cf83e776f9eb', 'WO-ST-003', '1006', 'Washer 10mm', 500, FALSE, 'Routine stock refill', 'finished', NOW(), NOW()),
    ('eaa0171c-96f9-4851-a75f-b1ae698e9564', 'WO-ST-004', '1003', 'Rubber Gasket', 80, TRUE, 'Urgent gasket replacement', 'pending', NOW(), NOW()),
    ('4057dad4-8526-4292-b4f0-21a0faf4302a', 'WO-ST-005', '1004', 'Steel Bolt 12mm', 120, FALSE, 'Line 2 replenishment', 'in_progress', NOW(), NOW()),
    ('50e12a65-24ff-4275-94a5-464485237251', 'WO-ST-006', '1006', 'Aluminum Bracket', 60, FALSE, 'Assembly line stock', 'finished', NOW(), NOW()),
    ('38cdf8b5-189b-45ae-8d96-dcef47a6e930', 'WO-ST-007', '1003', 'Spring Lock Washer', 300, FALSE, 'Routine stock refill', 'pending', NOW(), NOW()),
    ('b2908590-6897-4d09-b6bf-c60c07124038', 'WO-ST-008', '1004', 'Hex Bolt 6mm', 90, TRUE, 'Urgent line 4 replenishment', 'in_progress', NOW(), NOW()),
    ('216ef945-057f-4bff-9e17-20e99399b3f4', 'WO-ST-009', '1006', 'Plastic Cap', 250, FALSE, 'Routine stock refill', 'finished', NOW(), NOW()),
    ('78a7c3ac-1393-4e07-8a9d-374fc42ed56f', 'WO-ST-010', '1003', 'Cable Tie', 400, FALSE, 'Assembly line stock', 'pending', NOW(), NOW());

-- ===================== WIRE CUTTING DETAILS (10 itens) =====================
INSERT INTO wire_cutting_details (
    id, work_order_number, operator_number, wire_name, quantity, is_urgent, length_mm, reason, status, created_at, updated_at
) VALUES
    ('113ab2ce-4dbe-4442-93ec-fef496b1fde9', 'WO-WC-001', '1004', 'Copper 2.5mm', 10, FALSE, 1500.00, 'Panel wiring batch 1', 'pending', NOW(), NOW()),
    ('70debb84-54a9-4c06-9bd3-d2c0e754a25e', 'WO-WC-002', '1006', 'Copper 1.5mm', 25, TRUE, 800.00, 'Urgent panel wiring batch 2', 'in_progress', NOW(), NOW()),
    ('bd54127c-87b9-48cd-b6d8-f560b240486a', 'WO-WC-003', '1003', 'Aluminum 4mm', 5, FALSE, 2200.00, 'Panel wiring batch 3', 'finished', NOW(), NOW()),
    ('951d5b8a-3062-457a-8c69-3bcfeb81731d', 'WO-WC-004', '1004', 'Copper 2.5mm', 15, FALSE, 1200.00, 'Panel wiring batch 4', 'pending', NOW(), NOW()),
    ('6c13d100-4e48-4991-bdee-44259b1899b5', 'WO-WC-005', '1006', 'Copper 6mm', 8, TRUE, 3000.00, 'Urgent panel wiring batch 5', 'in_progress', NOW(), NOW()),
    ('59e5f778-be4d-4f43-85f7-54383e926a3f', 'WO-WC-006', '1003', 'Copper 1.5mm', 30, FALSE, 600.00, 'Panel wiring batch 6', 'finished', NOW(), NOW()),
    ('e4b3a038-5738-42ab-95c4-19e4a94e70ba', 'WO-WC-007', '1004', 'Aluminum 2.5mm', 12, FALSE, 1800.00, 'Panel wiring batch 7', 'pending', NOW(), NOW()),
    ('e069ccbb-7477-47a7-9ce3-dcc8600bf861', 'WO-WC-008', '1006', 'Copper 4mm', 20, TRUE, 950.00, 'Urgent panel wiring batch 8', 'in_progress', NOW(), NOW()),
    ('142b3bb2-816a-46e3-8832-b893fe9f4f81', 'WO-WC-009', '1003', 'Copper 2.5mm', 18, FALSE, 1100.00, 'Panel wiring batch 9', 'finished', NOW(), NOW()),
    ('823a6589-6281-4bf0-9501-5c438dbbe4aa', 'WO-WC-010', '1004', 'Aluminum 6mm', 6, FALSE, 2500.00, 'Panel wiring batch 10', 'pending', NOW(), NOW());
